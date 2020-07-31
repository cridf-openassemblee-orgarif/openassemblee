/** @jsx jsx */
import { css, jsx } from '@emotion/core';
import * as React from 'react';
import { colors } from '../../constants';
import { clearfix, domUid } from '../../utils';
import EluAutocomplete from './EluAutocomplete';
import { AppData, Associations, SelectedEluSource } from '../App';
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
    associations: Associations;
    hemicycle: HemicycleDTO;
    deleteMode: boolean;
    switchDeleteMode: () => void;
    hideAssociationsChairs: boolean;
    switchHideAssociations: () => void;
    saveProtoAssociations: () => void;
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

    private initialChairNumber = () =>
        this.increment(this.props.hemicycle.minChairNumber - 1);

    private increment = (chairNumber: number): number => {
        const newChairNumber = chairNumber + 1;
        if (this.props.associations.associationsByChair[newChairNumber]) {
            return this.increment(newChairNumber);
        } else if (newChairNumber > this.props.hemicycle.maxChairNumber) {
            return this.initialChairNumber();
        } else {
            return newChairNumber;
        }
    };

    componentDidUpdate(
        prevProps: Readonly<Props>,
        prevState: Readonly<State>
    ): void {
        const selectedChairNumber = this.props.selectedChairNumber;
        if (selectedChairNumber !== prevProps.selectedChairNumber) {
            if (!selectedChairNumber) {
                if (this.state.autoIncrement) {
                    if (prevProps.selectedChairNumber) {
                        this.props.updateSelectedChairNumber(
                            this.increment(prevProps.selectedChairNumber)
                        );
                    } else {
                        this.props.updateSelectedChairNumber(
                            this.initialChairNumber()
                        );
                    }
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
        if (
            (!prevProps.deleteMode && this.props.deleteMode) ||
            (!prevProps.hideAssociationsChairs &&
                this.props.hideAssociationsChairs)
        ) {
            this.setState(state => ({
                ...state,
                autoIncrement: false,
                chairInput: ''
            }));
        }
    }

    private switchAutoIncrement = () => {
        this.setState(state => ({
            ...state,
            autoIncrement: !state.autoIncrement
        }));
        if (!this.props.selectedChairNumber) {
            this.props.updateSelectedChairNumber(this.initialChairNumber());
        }
    };

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
                    margin: 10px 0;
                `}
            >
                <div
                    css={css`
                        display: flex;
                        flex-direction: row;
                    `}
                >
                    <div
                        css={css`
                            flex: 1;
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
                                &:disabled {
                                    background: ${colors.redBackground};
                                    border-color: ${colors.red};
                                }
                            `}
                            onValueChange={this.updateChairInput}
                            value={this.state.chairInput}
                            disabled={this.props.deleteMode}
                        />
                    </div>
                    <div
                        css={css`
                            flex: 5;
                            padding-left: 4px;
                        `}
                    >
                        <EluAutocomplete
                            selectedElu={this.props.selectedElu}
                            selectedEluSource={this.props.selectedEluSource}
                            updateSelectedElu={this.props.updateSelectedElu}
                            data={this.props.data}
                            deleteMode={this.props.deleteMode}
                        />
                    </div>
                    <div
                        css={css`
                            flex: 2;
                            padding-left: 4px;
                        `}
                    >
                        <div
                            css={css`
                                background: ${this.props.deleteMode
                                    ? colors.red
                                    : colors.redBackground};
                                border: 1px solid ${colors.red};
                                color: ${this.props.deleteMode
                                    ? colors.white
                                    : colors.red};
                                height: 36px;
                                margin: 2px;
                                border-radius: 2px;
                                padding-top: 3px;
                                text-align: center;
                                font-size: 11px;
                                cursor: pointer;
                                font-weight: bold;
                            `}
                            onClick={this.props.switchDeleteMode}
                        >
                            Mode
                            <br />
                            suppression
                        </div>
                    </div>
                    <div
                        css={css`
                            flex: 2;
                            padding-left: 4px;
                        `}
                    >
                        <div
                            css={css`
                                background: ${colors.blueBackground};
                                border: 1px solid ${colors.blueborder};
                                color: ${colors.blueborder};
                                height: 36px;
                                margin: 2px;
                                border-radius: 2px;
                                padding-top: 9px;
                                text-align: center;
                                font-size: 12px;
                                cursor: pointer;
                                font-weight: bold;
                            `}
                            onClick={this.props.saveProtoAssociations}
                        >
                            Enregistrer
                        </div>
                    </div>
                    <div
                        css={css`
                        display: none;
                            flex: 2;
                            padding-left: 4px;
                        `}
                    >
                        <div
                            css={css`
                                background: ${colors.blueBackground2};
                                border: 1px solid ${colors.blueborder2};
                                color: ${colors.blueborder2};
                                height: 36px;
                                margin: 2px;
                                border-radius: 2px;
                                padding-top: 9px;
                                text-align: center;
                                font-size: 12px;
                                cursor: pointer;
                                font-weight: bold;
                            `}
                        >
                            Figer
                        </div>
                    </div>
                </div>
                <div>
                    <label
                        htmlFor={autoIncrementCheckboxId}
                        css={
                            this.props.deleteMode
                                ? css`
                                      color: ${colors.grey};
                                  `
                                : undefined
                        }
                    >
                        <input
                            type="checkbox"
                            id={autoIncrementCheckboxId}
                            checked={this.state.autoIncrement}
                            onChange={this.switchAutoIncrement}
                            disabled={this.props.deleteMode}
                        />{' '}
                        Incrément auto
                    </label>
                    <label
                        htmlFor={hideAssociationsCheckboxId}
                        css={css`
                            margin-left: 10px;
                            ${this.props.deleteMode
                                ? css`
                                      color: ${colors.grey};
                                  `
                                : undefined};
                        `}
                    >
                        <input
                            type="checkbox"
                            id={hideAssociationsCheckboxId}
                            checked={this.props.hideAssociationsChairs}
                            onChange={this.props.switchHideAssociations}
                            disabled={this.props.deleteMode}
                        />{' '}
                        Cacher les sièges pris
                    </label>
                </div>
            </div>
        );
    }
}
