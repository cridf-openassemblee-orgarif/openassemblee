/** @jsx jsx */
import { css, jsx } from '@emotion/core';
import React from 'react';
import ReactDOM from 'react-dom';
import App from './component/App';
import { injector } from './service/injector';

window.addEventListener('resize', () =>
    injector().applicationEventBus.publish('window_resized_event')
);

// Ce code pour redimensionner l'assemblee à l'agrandissement/rétrécissement du menu admintle
// setTimeout car sinon document.body est null à l'éxécution du code
setTimeout(() => {
    new MutationObserver(function(event) {
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
        characterData: false
    });
}, 0);

global.loadAssemblee = () => {
    ReactDOM.render(<App />, document.getElementById('assemblee-app'));
};

if (global.devHost) {
    global.loadAssemblee();
}
