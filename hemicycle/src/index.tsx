/** @jsx jsx */
import { jsx } from '@emotion/core';
import './utilities-impl';
import ReactDOM from 'react-dom';
import App from './component/App';
import { injector } from './service/injector';
import { options } from './constants';
import { instanciateNominalNumber } from './domain/nominal';

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

global.loadHemicycle = (props: { planId: number; isProjet: boolean }) => {
    ReactDOM.render(
        <App
            planId={instanciateNominalNumber(props.planId)}
            isProjet={props.isProjet}
        />,
        document.getElementById('hemicycle-app')
    );
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
    if (planId && isProjet) {
        global.loadHemicycle({
            planId: parseInt(planId, 10),
            isProjet: isProjet === 'true',
        });
    } else {
        ReactDOM.render(
            <div>
                Mettre les paramètre 'planId' et 'isProjet' en url, par ex
                "?planId=8&isProjet=true"
            </div>,
            document.getElementById('hemicycle-app')
        );
    }
}
