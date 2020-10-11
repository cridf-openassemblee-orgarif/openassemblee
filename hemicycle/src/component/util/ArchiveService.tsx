/** @jsx jsx */
import { jsx } from '@emotion/core';
import * as React from 'react';
import { PlanId } from '../../domain/nominal';
import { injector } from '../../service/injector';
import { urls } from '../../constants';
import { HemicycleArchiveCreationDTO } from '../../domain/ws';
import { HemicyclePlanAssociations } from '../../domain/hemicycle';
import Hemicycle from '../Hemicycle';
import { ReactElement } from 'react';
import * as ReactDomServer from 'react-dom/server';
import { RawElus } from './DataService';
import { MapData } from '../App';

export class ArchiveService {
    saveArchive = (
        planId: PlanId,
        rawElus: RawElus,
        hemicycle: HemicyclePlanAssociations,
        mapData: MapData,
        then: () => void
    ) => {
        const renderReact = (svgElement: ReactElement) => {
            const svg = ReactDomServer.renderToString(svgElement);
            // FIXMENOW [doc] da fuck Batik semble ne pas aimer ça.... parce que je parse mal ?
            // FIXMENOW rechecker en fait....
            return '<?xml version="1.0" encoding="UTF-8"?>' + svg;
            // return finalSvg;
        };
        const svg = renderReact(
            <Hemicycle
                width={1600}
                height={1000}
                eluById={mapData.eluById}
                groupePolitiques={rawElus.groupePolitiques}
                groupePolitiqueById={mapData.groupePolitiqueById}
                associationByChair={mapData.associationByChair}
                configurationRendu={hemicycle.configurationRendu}
                selectedChairNumber={undefined}
                updateSelectedChairNumber={() => {}}
                hideAssociationsChairs={false}
                removeAssociation={() => {}}
                deleteMode={false}
                printMode={true}
            />
        );
        const dto: HemicycleArchiveCreationDTO = {
            planId: planId,
            data: {
                associations: hemicycle.associations,
                ...rawElus,
            },
            svgPlan: svg,
        };
        injector()
            .httpService.post(injector().urlBase + urls.hemicycleArchives, dto)
            .then(() => setTimeout(then, 500));
    };
}
