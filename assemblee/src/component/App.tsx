import React from 'react';
import Assemblee from './Assemblee';
import { injector } from '../service/injector';

interface State {
    assemblee?: AssembleeDTO;
}

export default class App extends React.PureComponent<{}, State> {
    state: State = {
        assemblee: undefined
    };

    componentDidMount(): void {
        injector()
            .httpService.get(injector().urlBase + '/api/assemblee')
            .then(a => {
                this.setState(state => ({ ...state, assemblee: a.body }));
            });
    }

    public render() {
        return (
            <div>
                {this.state.assemblee && (
                    <Assemblee assemblee={this.state.assemblee} />
                )}
            </div>
        );
    }
}
