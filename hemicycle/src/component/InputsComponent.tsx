/** @jsx jsx */
import { css, jsx } from '@emotion/core';
import React from 'react';
import { colors } from '../constants';
import { clearfix, domUid } from '../utils';
import EluSelectionComponent from './EluSelectionComponent';
import { AppData, Selections } from './App';

interface Props {
    selections: Selections;
    data: AppData;
}

interface State {
    chairInput: string;
    chairInputIsValid: boolean;
    autoIncrement: boolean;
}

export default class InputsComponent extends React.PureComponent<Props, State> {
    state = {
        chairInput: this.props.selections.selectedChairNumber
            ? this.props.selections.selectedChairNumber.toString()
            : '',
        chairInputIsValid: true,
        autoIncrement: false
    };

    componentWillUpdate(
        nextProps: Readonly<Props>,
        nextState: Readonly<State>,
        nextContext: any
    ): void {
        const selectedChairNumber = nextProps.selections.selectedChairNumber;
        if (selectedChairNumber !== this.props.selections.selectedChairNumber) {
            if (selectedChairNumber) {
                this.setState(state => ({
                    ...state,
                    chairInput: selectedChairNumber.toString()
                }));
            } else {
                if (
                    this.props.selections.selectedChairNumber &&
                    this.state.autoIncrement
                ) {
                    const newChairNumber =
                        this.props.selections.selectedChairNumber + 1;
                    this.props.selections.updateSelectedChairNumber(
                        newChairNumber
                    );
                } else {
                    this.setState(state => ({
                        ...state,
                        chairInput: ''
                    }));
                }
            }
        }
    }

    private switchAutoIncrement = () =>
        this.setState(state => ({
            ...state,
            autoIncrement: !state.autoIncrement
        }));

    private updateChairInput = (e: React.ChangeEvent<HTMLInputElement>) => {
        const chairInput = e.target.value;
        this.setState(state => ({ ...state, chairInput }));
        const chairNumber = parseInt(chairInput);
        this.props.selections.updateSelectedChairNumber(chairNumber);
    };

    public render() {
        const checkboxId = domUid();
        return (
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
                            font-size: 20px;
                            width: 100%;
                            text-align: center;
                            height: 40px;
                            &:focus {
                                border: 1px solid ${colors.black};
                            }
                        `}
                        onChange={this.updateChairInput}
                        value={this.state.chairInput}
                    />
                    <label htmlFor={checkboxId}>
                        <input
                            type="checkbox"
                            id={checkboxId}
                            value={this.state.autoIncrement.toString()}
                            onChange={this.switchAutoIncrement}
                        />
                        Incrément auto
                    </label>
                </div>
                <div
                    css={css`
                        position: relative;
                        float: left;
                        width: 75%;
                        padding-left: 4px;
                    `}
                >
                    <EluSelectionComponent
                        selections={this.props.selections}
                        data={this.props.data}
                    />
                </div>
            </div>
        );
    }
}
