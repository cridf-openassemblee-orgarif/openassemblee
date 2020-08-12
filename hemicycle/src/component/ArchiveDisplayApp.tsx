/** @jsx jsx */
import { jsx } from '@emotion/core';
import * as React from 'react';
import SizingContainer from './util/SizingContainer';
import Hemicycle from './Hemicycle';
import { Elu, GroupePolitique } from '../domain/elu';
import {
    ArchiveId,
    ChairNumber,
    EluId,
    GroupePolitiqueId,
} from '../domain/nominal';
import { Dict } from '../utils';
import { Association } from '../domain/hemicycle';
import { HemicycleConfigurationRendu } from '../domain/assemblee';
import { injector } from '../service/injector';
import { urls } from '../constants';
import { HemicycleArchiveDataWithConfigurationDTO } from '../domain/ws';
import { associationMaps, rawElusMaps } from './App';

interface Props {
    archiveId: ArchiveId;
}

interface State {
    rawElus?: {
        elus: Elu[];
        groupePolitiques: GroupePolitique[];
    };
    hemicycle?: {
        associations: Association[];
        configurationRendu: HemicycleConfigurationRendu;
    };
    maps?: {
        eluById: Dict<EluId, Elu>;
        groupePolitiqueById: Dict<GroupePolitiqueId, GroupePolitique>;
        associationByChair: Dict<ChairNumber, Association>;
    };
}

export default class ArchiveDisplayApp extends React.PureComponent<
    Props,
    State
> {
    state: State = {};

    componentDidMount(): void {
        injector()
            .httpService.get(
                injector().urlBase +
                    urls.hemicycleArchivesData +
                    '/' +
                    this.props.archiveId
            )
            .then((response) => {
                const r = response.body as HemicycleArchiveDataWithConfigurationDTO;
                const rawElus = {
                    elus: r.data.elus,
                    groupePolitiques: r.data.groupePolitiques,
                };
                const { eluById, groupePolitiqueById } = rawElusMaps(rawElus);
                const { associationByChair } = associationMaps(
                    r.data.associations
                );
                this.setState((state) => ({
                    rawElus,
                    hemicycle: {
                        associations: r.data.associations,
                        configurationRendu: r.rendu,
                    },
                    maps: {
                        eluById,
                        associationByChair,
                        groupePolitiqueById,
                    },
                }));
            });
    }

    public render() {
        if (!this.state.rawElus || !this.state.maps || !this.state.hemicycle) {
            return null;
        }
        const { rawElus, maps, hemicycle } = this.state;
        return (
            <SizingContainer
                render={(width: number, height: number) => (
                    <Hemicycle
                        width={width}
                        height={height}
                        eluById={maps.eluById}
                        groupePolitiques={rawElus.groupePolitiques}
                        groupePolitiqueById={maps.groupePolitiqueById}
                        associationByChair={maps.associationByChair}
                        configurationRendu={hemicycle.configurationRendu}
                        selectedChairNumber={undefined}
                        updateSelectedChairNumber={() => {}}
                        hideAssociationsChairs={false}
                        removeAssociation={() => {}}
                        deleteMode={false}
                        printMode={false}
                    />
                )}
            />
        );
    }
}
