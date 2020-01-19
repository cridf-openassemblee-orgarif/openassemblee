/** @jsx jsx */
import { css, jsx } from '@emotion/core';
import React from 'react';
import { injector } from '../service/injector';
import { clearfix } from '../utils';
import SizingContainer from './SizingContainer';
import Hemicycle from './Hemicycle';
import SelectionComponent from './SelectionComponent';

interface State {
    hemicycle?: HemicycleDTO;
    eluListDTOs?: EluListDTO[];
    selectedChairNumber?: number;
    selectedElu?: EluListDTO;
    associations: Dict<number, EluListDTO>;
}

export default class App extends React.PureComponent<{}, State> {
    state: State = {
        hemicycle: undefined,
        eluListDTOs: undefined,
        selectedChairNumber: undefined,
        selectedElu: undefined,
        associations: {}
    };

    componentDidMount(): void {
        injector()
            .httpService.get(injector().urlBase + '/api/hemicycle')
            .then(a => {
                this.setState(state => ({
                    ...state,
                    hemicycle: a.body
                }));
            });
        injector()
            .httpService.get(injector().urlBase + '/api/elus')
            .then(a => {
                const eluListDTOs = a.body as EluListDTO[];
                this.setState(state => ({
                    ...state,
                    eluListDTOs
                }));
            });
    }

    private checkAssociation = () => {
        this.setState(state => {
            const selectedChairNumber = state.selectedChairNumber;
            if (selectedChairNumber && state.selectedElu) {
                return {
                    ...state,
                    associations: {
                        ...state.associations,
                        [selectedChairNumber]: state.selectedElu
                    },
                    selectedChairNumber: undefined,
                    selectedElu: undefined
                };
            } else {
                return state;
            }
        });
    };

    private updateSelectedChairNumber = (selectedChairNumber: number) => {
        this.setState(state => ({
            ...state,
            selectedChairNumber
        }));
        this.checkAssociation();
    };

    private updateSelectedElu = (selectedElu: EluListDTO) => {
        this.setState(state => ({ ...state, selectedElu }));
        this.checkAssociation();
    };

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
                            {this.state.hemicycle && (
                                <Hemicycle
                                    hemicycle={this.state.hemicycle}
                                    width={(width * 3) / 4}
                                    height={height}
                                    selectedChairNumber={
                                        this.state.selectedChairNumber
                                    }
                                    updateChairNumber={
                                        this.updateSelectedChairNumber
                                    }
                                    associations={this.state.associations}
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
                                    selectionMode={false}
                                    selectedChairNumber={
                                        this.state.selectedChairNumber
                                    }
                                    updateSelectedChairNumber={
                                        this.updateSelectedChairNumber
                                    }
                                    updateSelectedElu={this.updateSelectedElu}
                                    associations={this.state.associations}
                                />
                            )}
                        </div>
                    </div>
                )}
            />
        );
    }
}
