/** @jsx jsx */
import { css, jsx } from '@emotion/core';
import * as React from 'react';
import SizingContainer from './util/SizingContainer';
import Hemicycle from './Hemicycle';
import { Elu, GroupePolitique } from '../domain/elu';
import { ArchiveId } from '../domain/nominal';
import { Association } from '../domain/hemicycle';
import { HemicycleConfigurationRendu } from '../domain/assemblee';
import { injector } from '../service/injector';
import { urls } from '../constants';
import { HemicycleArchiveDataWithConfigurationDTO } from '../domain/ws';
import { PrintButton } from './input/InputsComponent';
import { Errors } from './util/errors';
import { HemicycleMapData } from './App';

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
    maps?: HemicycleMapData;
}

const buttonHeight = 42;

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
                const {
                    eluById,
                    groupePolitiqueById,
                } = injector().dataService.rawElusMaps(rawElus);
                const {
                    associationByChair,
                } = injector().dataService.associationMaps(r.data.associations);
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

    print = () => {
        if (!this.state.rawElus || !this.state.hemicycle || !this.state.maps) {
            throw Errors._b7d84f98();
        }
        injector().archiveService.printPlanFromData(
            this.state.rawElus,
            this.state.hemicycle,
            this.state.maps
        );
    };

    public render() {
        if (!this.state.rawElus || !this.state.maps || !this.state.hemicycle) {
            return null;
        }
        const { rawElus, maps, hemicycle } = this.state;
        return (
            <SizingContainer
                render={(width: number, height: number) => (
                    <React.Fragment>
                        <div
                            css={css`
                                width: 200px;
                                margin: auto;
                                height: ${buttonHeight}px;
                            `}
                        >
                            <PrintButton print={this.print} />
                        </div>
                        <Hemicycle
                            width={width}
                            height={height - buttonHeight}
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
                    </React.Fragment>
                )}
            />
        );
    }
}
