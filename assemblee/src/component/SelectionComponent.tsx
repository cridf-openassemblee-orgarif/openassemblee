/** @jsx jsx */
import { css, jsx } from '@emotion/core';
import React, { ChangeEventHandler } from 'react';
import GroupePolitiqueComponent from './GroupePolitiqueComponent';
import { colors } from '../constants';
import { clearfix } from '../utils';

interface Props {
    eluListDTOs: EluListDTO[];
    selectionMode: boolean;
    selectedChairNumber?: number;
    updateChairNumber: (chairNumber: number) => void;
}

interface State {
    chairInput: string;
    chairInputIsValid: boolean;
    searchEluInput: string;
}

export default class SelectionComponent extends React.PureComponent<
    Props,
    State
> {
    state = {
        chairInput: '',
        chairInputIsValid: true,
        searchEluInput: ''
    };

    componentDidMount(): void {
        const selectedChairNumber = this.props.selectedChairNumber;
        if (selectedChairNumber) {
            this.setState(state => ({
                ...state,
                chairInput: selectedChairNumber.toString()
            }));
        }
    }

    componentWillUpdate(
        nextProps: Readonly<Props>,
        nextState: Readonly<State>,
        nextContext: any
    ): void {
        const selectedChairNumber = nextProps.selectedChairNumber;
        if (
            selectedChairNumber !== this.props.selectedChairNumber &&
            selectedChairNumber
        ) {
            this.setState(state => ({
                ...state,
                chairInput: selectedChairNumber.toString()
            }));
        }
    }

    private updateChairInput = (e: React.ChangeEvent<HTMLInputElement>) => {
        const chairInput = e.target.value;
        this.setState(state => ({ ...state, chairInput }));
        const chairNumber = parseInt(chairInput);
        this.props.updateChairNumber(chairNumber);
    };

    private updateSearchEluInput = (e: React.ChangeEvent<HTMLInputElement>) => {
        const searchEluInput = e.target.value;
        this.setState(state => ({ ...state, searchEluInput }));
    };

    public render() {
        const elusParGroupe = {} as any;
        const groupes = {} as any;
        this.props.eluListDTOs.forEach(e => {
            if (e.groupePolitique) {
                if (!elusParGroupe[e.groupePolitique.id]) {
                    elusParGroupe[e.groupePolitique.id] = [];
                }
                elusParGroupe[e.groupePolitique.id].push(e.elu);
                groupes[e.groupePolitique.id] = e.groupePolitique;
                // } else {
                //     if (!elusParGroupe[sansGroupePolitique]) {
                //         elusParGroupe[sansGroupePolitique] = [];
                //     }
                //     elusParGroupe[sansGroupePolitique].push(e.elu);
                //     groupes[sansGroupePolitique] = {
                //         nomCourt: 'Sans',
                //         couleur: colors.greyWithoutSharp
                //     } as Partial<GroupePolitique>;
            }
        });
        return (
            <div
                css={css`
                    //padding: 10px;
                    //margin-right: 10px;
                    position: relative;
                    height: 100%;
                    overflow: scroll;
                `}
            >
                <div
                    css={css`
                        ${clearfix};
                        margin: 10px 0;
                    `}
                >
                    <div
                        css={css`
                            position: relative;
                            float: left;
                            width: 25%;
                        `}
                    >
                        <input
                            css={css`
                                border: 1px solid ${colors.grey};
                                outline: none;
                                font-size: 30px;
                                width: 100%;
                                &:focus {
                                    border: 1px solid ${colors.blue};
                                }
                            `}
                            onChange={this.updateChairInput}
                            value={this.state.chairInput}
                        />
                    </div>
                    <div
                        css={css`
                            position: relative;
                            float: left;
                            width: 75%;
                            padding-left: 4px;
                        `}
                    >
                        <input
                            css={css`
                            position: relative;
                                border: 1px solid ${colors.grey};
                                outline: none;
                                font-size: 30px;
                                width: 100%;
                                &:focus {
                                    border: 1px solid ${colors.blue};
                                }
                            `}
                            onChange={this.updateSearchEluInput}
                            value={this.state.searchEluInput}
                        />
                    </div>
                </div>
                <div
                    css={css`
                        overflow: scroll;
                    `}
                >
                    {Object.keys(elusParGroupe).map(gp => {
                        const groupe = groupes[gp];
                        if (!groupe) {
                            throw new Error();
                        }
                        const elus = elusParGroupe[gp];
                        if (!elus) {
                            throw new Error();
                        }
                        return (
                            <GroupePolitiqueComponent
                                key={groupe.id}
                                groupePolitique={groupe}
                                elus={elus}
                            />
                        );
                    })}
                </div>
            </div>
        );
    }
}
