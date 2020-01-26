/** @jsx jsx */
import { css, jsx } from '@emotion/core';
import React from 'react';
import { colors } from '../constants';
import { AppData, Selections } from './App';

interface Props {
    selections: Selections;
    data: AppData;
}

interface State {
    chairInput: string;
    chairInputIsValid: boolean;
}

export default class InputsComponent extends React.PureComponent<Props, State> {
    state = {
        chairInput: this.props.selections.selectedChairNumber
            ? this.props.selections.selectedChairNumber.toString()
            : '',
        chairInputIsValid: true
    };

    componentWillUpdate(
        nextProps: Readonly<Props>,
        nextState: Readonly<State>,
        nextContext: any
    ): void {
        const selectedChairNumber = nextProps.selections.selectedChairNumber;
        if (selectedChairNumber !== this.props.selections.selectedChairNumber) {
            this.setState(state => ({
                ...state,
                chairInput: selectedChairNumber
                    ? selectedChairNumber.toString()
                    : ''
            }));
        }
    }

    private updateChairInput = (e: React.ChangeEvent<HTMLInputElement>) => {
        const chairInput = e.target.value;
        this.setState(state => ({ ...state, chairInput }));
        const chairNumber = parseInt(chairInput);
        this.props.selections.updateSelectedChairNumber(chairNumber);
    };

    public render() {
        return (
            <div
                css={css`
                    margin: 10px 0;
                    position: relative;
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
            </div>
        );
    }
}
