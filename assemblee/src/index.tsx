/** @jsx jsx */
import { css, jsx } from '@emotion/core';
import React from 'react';
import ReactDOM from 'react-dom';
import App from './component/App';
import { injector } from './service/injector';

window.addEventListener('resize', () =>
    injector().applicationEventBus.publish('window_resized_event')
);

global.loadAssemblee = () => {
    ReactDOM.render(<App />, document.getElementById('assemblee-app'));
};

if (global.devHost) {
    global.loadAssemblee();
}
