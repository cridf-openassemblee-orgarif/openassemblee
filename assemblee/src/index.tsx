import React from 'react';
import ReactDOM from 'react-dom';
import App from './component/App';

global.loadAssemblee = () => {
    ReactDOM.render(<App />, document.getElementById('assemblee-app'));
};

if (global.devHost) {
    global.loadAssemblee();
}
