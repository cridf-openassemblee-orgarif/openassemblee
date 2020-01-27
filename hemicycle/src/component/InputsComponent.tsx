/** @jsx jsx */
import { css, jsx } from '@emotion/core';
import React from 'react';
import { colors } from '../constants';
import { clearfix, domUid } from '../utils';
import EluSelectionComponent from './EluSelectionComponent';
import { AppData, SelectedEluSource } from './App';
import DelayedChangeInput from './DelayedChangeInput';

interface Props {
    selectedChairNumber?: number;
    selectedElu?: Elu;
    selectedEluSource?: SelectedEluSource;
    updateSelectedChairNumber: (selectedChairNumber: number) => void;
    updateSelectedElu: (
        selectedElu: Elu | undefined,
        source: SelectedEluSource
    ) => void;
    data: AppData;
    hideAssociationsChairs: boolean;
    switchHideAssociations: () => void;
}

interface State {
    chairInput: string;
    chairInputIsValid: boolean;
    autoIncrement: boolean;
}

export default class InputsComponent extends React.PureComponent<Props, State> {
    state = {
        chairInput: this.props.selectedChairNumber
            ? this.props.selectedChairNumber.toString()
            : '',
        chairInputIsValid: true,
        autoIncrement: false
    };

    componentDidUpdate(
        prevProps: Readonly<Props>,
        prevState: Readonly<State>
    ): void {
        const selectedChairNumber = this.props.selectedChairNumber;
        if (selectedChairNumber !== prevProps.selectedChairNumber) {
            if (!selectedChairNumber) {
                if (this.state.autoIncrement && prevProps.selectedChairNumber) {
                    const newChairNumber = prevProps.selectedChairNumber + 1;
                    this.props.updateSelectedChairNumber(newChairNumber);
                } else {
                    this.setState(state => ({
                        ...state,
                        chairInput: ''
                    }));
                }
            } else {
                this.setState(state => ({
                    ...state,
                    chairInput: selectedChairNumber.toString()
                }));
            }
        }
    }

    private switchAutoIncrement = () =>
        this.setState(state => ({
            ...state,
            autoIncrement: !state.autoIncrement
        }));

    private updateChairInput = (chairInput: string) => {
        this.setState(state => ({ ...state, chairInput }));
        const chairNumber = parseInt(chairInput);
        this.props.updateSelectedChairNumber(chairNumber);
    };

    public render() {
        const autoIncrementCheckboxId = domUid();
        const hideAssociationsCheckboxId = domUid();
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
                    <DelayedChangeInput
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
                        onValueChange={this.updateChairInput}
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
                    <EluSelectionComponent
                        selectedElu={this.props.selectedElu}
                        selectedEluSource={this.props.selectedEluSource}
                        updateSelectedElu={this.props.updateSelectedElu}
                        data={this.props.data}
                    />
                </div>
                <label htmlFor={autoIncrementCheckboxId}>
                    <input
                        type="checkbox"
                        id={autoIncrementCheckboxId}
                        value={this.state.autoIncrement.toString()}
                        onChange={this.switchAutoIncrement}
                    />
                    Incrément auto
                </label>
                <label
                    htmlFor={hideAssociationsCheckboxId}
                    css={css`
                        margin-left: 10px;
                    `}
                >
                    <input
                        type="checkbox"
                        id={hideAssociationsCheckboxId}
                        value={this.props.hideAssociationsChairs.toString()}
                        onChange={this.props.switchHideAssociations}
                    />
                    Cacher les associations
                </label>
            </div>
        );
    }
}
