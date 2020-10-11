/** @jsx jsx */
import { jsx } from '@emotion/core';
import './utilities-impl';
import ReactDOM from 'react-dom';
import App from './component/App';
import { injector } from './service/injector';
import { options } from './constants';
import ArchiveDisplayApp from './component/ArchiveDisplayApp';

window.addEventListener('resize', () =>
    injector().applicationEventBus.publish('window_resized_event')
);

// Ce code pour redimensionner l'hemicycle à l'agrandissement/rétrécissement du menu admintle
// setTimeout car sinon document.body est null à l'éxécution du code
setTimeout(() => {
    new MutationObserver(function (event) {
        setTimeout(
            () => {
                injector().applicationEventBus.publish('window_resized_event');
            },
            // 500ms est le temps de l'animation d'agrandissement/rétrécissement
            550
        );
    }).observe(document.body, {
        attributes: true,
        attributeFilter: ['class'],
        childList: false,
        characterData: false,
    });
}, 0);

global.saveArchive = (props: { planId: any; then: () => void }) => {
    injector()
        .dataService.fetchData(props.planId)
        .then((d) => {
            const maps1 = injector().dataService.rawElusMaps(d.rawElus);
            const maps2 = injector().dataService.associationMaps(
                d.hemicycle.associations
            );
            injector().archiveService.saveArchive(
                props.planId,
                d.rawElus,
                d.hemicycle,
                {
                    ...maps1,
                    ...maps2,
                },
                props.then
            );
        });
};

global.loadHemicycle = (props: { planId: any; isProjet: boolean }) => {
    ReactDOM.render(
        <App planId={props.planId} isProjet={props.isProjet} />,
        document.getElementById('hemicycle-app')
    );
};

global.loadHemicycleArchive = (props: { archiveId: any }) => {
    ReactDOM.render(
        <ArchiveDisplayApp archiveId={props.archiveId} />,
        document.getElementById('hemicycle-app')
    );
};

global.printPlan = (svgPlan: string) => {
    injector().archiveService.printPlan(svgPlan);
};

global.activateDebug = () => {
    options.debug = true;
    injector().applicationEventBus.publish('activate_debug');
};

if (global.devHost) {
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const planId = urlParams.get('planId');
    const isProjet = urlParams.get('isProjet');
    const archiveId = urlParams.get('archiveId');
    if (planId && isProjet) {
        global.loadHemicycle({
            planId: parseInt(planId, 10),
            isProjet: isProjet === 'true',
        });
    } else if (archiveId) {
        global.loadHemicycleArchive({
            archiveId: parseInt(archiveId, 10),
        });
    } else {
        ReactDOM.render(
            <div>
                Mettre les paramètre 'planId' et 'isProjet' en url, par ex
                "?planId=8&isProjet=true".
                <br />
                Ou archiveId
            </div>,
            document.getElementById('hemicycle-app')
        );
    }
}
