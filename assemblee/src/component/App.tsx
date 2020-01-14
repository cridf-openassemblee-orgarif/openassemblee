/** @jsx jsx */
import { css, jsx } from '@emotion/core';
import React from 'react';
import { injector } from '../service/injector';
import { clearfix } from '../utils';
import SizingContainer from './SizingContainer';
import Assemblee from './Assemblee';
import SelectionComponent from './SelectionComponent';

interface State {
    assemblee?: AssembleeDTO;
    eluListDTOs?: EluListDTO[];
}

export default class App extends React.PureComponent<{}, State> {
    state: State = {
        assemblee: undefined,
        eluListDTOs: undefined
    };

    componentDidMount(): void {
        injector()
            .httpService.get(injector().urlBase + '/api/assemblee')
            .then(a => {
                this.setState(state => ({ ...state, assemblee: a.body }));
            });
        injector()
            .httpService.get(injector().urlBase + '/api/elus')
            .then(a => {
                this.setState(state => ({ ...state, eluListDTOs: a.body }));
            });
    }

    public render() {
        return (
            <SizingContainer
                render={(width: number, height: number) => (
                    <div
                        css={css`
                            ${clearfix};
                            width: 100%;
                            height: ${height}px;
                        `}
                    >
                        <div
                            css={css`
                                float: left;
                                width: ${(width * 3) / 4}px;
                                height: ${height}px;
                            `}
                        >
                            {this.state.assemblee && (
                                <Assemblee
                                    assemblee={this.state.assemblee}
                                    width={(width * 3) / 4}
                                    height={height}
                                />
                            )}
                        </div>
                        <div
                            css={css`
                                float: left;
                                width: ${width / 4}px;
                                height: ${height}px;
                            `}
                        >
                            {this.state.eluListDTOs && (
                                <SelectionComponent
                                    eluListDTOs={this.state.eluListDTOs}
                                />
                            )}
                        </div>
                    </div>
                )}
            />
        );
    }
}
