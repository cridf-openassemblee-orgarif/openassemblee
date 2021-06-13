/** @jsx jsx */
import { jsx } from '@emotion/core';
import { ReactElement } from 'react';
import { PlanId } from '../../domain/nominal';
import { injector } from '../../service/injector';
import { urls } from '../../constants';
import { HemicycleArchiveCreationDTO } from '../../domain/ws';
import { HemicyclePlanAssociations } from '../../domain/hemicycle';
import Hemicycle from '../Hemicycle';
import * as ReactDomServer from 'react-dom/server';
import { RawElus } from './DataService';
import { HemicycleMapData, MapData } from '../App';
import { Errors } from './errors';

export class ArchiveService {
    private svg = (
        rawElus: RawElus,
        hemicycle: HemicyclePlanAssociations,
        mapData: HemicycleMapData
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
        return svg;
    };

    printPlan = (svgPlan: string) => {
        const url = 'data:image/svg+xml;charset=utf-8,' + svgPlan;
        const image = new Image();
        image.src = url;
        const printWindow = window.open('', 'PrintMap');
        if (!printWindow) {
            throw Errors._fec8a42c();
        }
        printWindow.document.writeln(svgPlan);
        printWindow.document.writeln(
            '<style>@page {size: A3 landscape;}</style>'
        );
        printWindow.document.close();
        printWindow.print();
    };

    printPlanFromData = (
        rawElus: RawElus,
        hemicycle: HemicyclePlanAssociations,
        mapData: HemicycleMapData
    ) => {
        const svgPlan = this.svg(rawElus, hemicycle, mapData);
        const url = 'data:image/svg+xml;charset=utf-8,' + svgPlan;
        const image = new Image();
        image.src = url;
        const printWindow = window.open('', 'PrintMap');
        if (!printWindow) {
            throw Errors._fec8a42c();
        }
        printWindow.document.writeln(svgPlan);
        printWindow.document.writeln(
            '<style>@page {size: A3 landscape;}</style>'
        );
        printWindow.document.close();
        printWindow.print();
    };

    saveArchive = (
        planId: PlanId,
        rawElus: RawElus,
        hemicycle: HemicyclePlanAssociations,
        mapData: MapData,
        then: () => void
    ) => {
        const svg = this.svg(rawElus, hemicycle, mapData);
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
