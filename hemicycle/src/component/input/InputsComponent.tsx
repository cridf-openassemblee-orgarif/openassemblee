/** @jsx jsx */
import { css, jsx } from '@emotion/core';
import * as React from 'react';
import { colors } from '../../constants';
import { Dict, domUid, getOrNull } from '../../utils';
import EluAutocomplete from './EluAutocomplete';
import { SelectedEluSource, Selection } from '../App';
import DelayedChangeInput from './DelayedChangeInput';
import LoadingIcon from '../util/LoadingIcon';
import { Errors } from '../util/errors';
import { Elu, GroupePolitique } from '../../domain/elu';
import { HemicycleConfigurationRendu } from '../../domain/assemblee';
import {
    ChairNumber,
    EluId,
    GroupePolitiqueId,
    instanciateNominalNumber,
    numberifyNominalNumber,
} from '../../domain/nominal';
import { Association } from '../../domain/hemicycle';

export const inputComponentHeight = 70;

export const PrintButton = (props: { print: () => void }) => (
    <div
        css={css`
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
            onClick={props.print}
        >
            Imprimer
        </div>
    </div>
);

interface Props {
    selection: Selection;
    elus: Elu[];
    eluById: Dict<EluId, Elu>;
    groupePolitiqueById: Dict<GroupePolitiqueId, GroupePolitique>;
    associationByChair: Dict<ChairNumber, Association>;
    updateSelectedChairNumber: (selectedChairNumber: ChairNumber) => void;
    updateSelectedEluId: (
        selectedEluId: EluId | undefined,
        source: SelectedEluSource
    ) => void;
    hemicycleConfigurationRendu: HemicycleConfigurationRendu;
    deleteMode: boolean;
    switchDeleteMode: () => void;
    hideAssociationsChairs: boolean;
    switchHideAssociations: () => void;
    savePlan: (then: () => void) => void;
    archive?: (then: () => void) => void;
    print: () => void;
}

interface State {
    chairInput: string;
    chairInputIsValid: boolean;
    autoIncrement: boolean;
    isSaving: boolean;
    isArchiving: boolean;
}

export default class InputsComponent extends React.PureComponent<Props, State> {
    state = {
        chairInput: this.props.selection.selectedChairNumber
            ? this.props.selection.selectedChairNumber.toString()
            : '',
        chairInputIsValid: true,
        autoIncrement: false,
        isSaving: false,
        isArchiving: false,
    };

    private initialChairNumber = () =>
        this.increment(
            instanciateNominalNumber(
                numberifyNominalNumber(
                    this.props.hemicycleConfigurationRendu.minChairNumber
                ) - 1
            )
        );

    private increment = (chairNumber: ChairNumber): ChairNumber => {
        const newChairNumber = instanciateNominalNumber<ChairNumber>(
            numberifyNominalNumber(chairNumber) + 1
        );
        if (getOrNull(this.props.associationByChair, newChairNumber)) {
            return this.increment(newChairNumber);
        } else if (
            numberifyNominalNumber(newChairNumber) >
            numberifyNominalNumber(
                this.props.hemicycleConfigurationRendu.maxChairNumber
            )
        ) {
            return this.initialChairNumber();
        } else {
            return newChairNumber;
        }
    };

    componentDidUpdate(
        prevProps: Readonly<Props>,
        prevState: Readonly<State>
    ): void {
        const selectedChairNumber = this.props.selection.selectedChairNumber;
        if (selectedChairNumber !== prevProps.selection.selectedChairNumber) {
            if (!selectedChairNumber) {
                if (this.state.autoIncrement) {
                    if (prevProps.selection.selectedChairNumber) {
                        this.props.updateSelectedChairNumber(
                            this.increment(
                                prevProps.selection.selectedChairNumber
                            )
                        );
                    } else {
                        this.props.updateSelectedChairNumber(
                            this.initialChairNumber()
                        );
                    }
                } else {
                    this.setState((state) => ({
                        ...state,
                        chairInput: '',
                    }));
                }
            } else {
                this.setState((state) => ({
                    ...state,
                    chairInput: selectedChairNumber.toString(),
                }));
            }
        }
        if (
            (!prevProps.deleteMode && this.props.deleteMode) ||
            (!prevProps.hideAssociationsChairs &&
                this.props.hideAssociationsChairs)
        ) {
            this.setState((state) => ({
                ...state,
                autoIncrement: false,
                chairInput: '',
            }));
        }
    }

    private switchAutoIncrement = () => {
        this.setState((state) => ({
            ...state,
            autoIncrement: !state.autoIncrement,
        }));
        if (!this.props.selection.selectedChairNumber) {
            this.props.updateSelectedChairNumber(this.initialChairNumber());
        }
    };

    private updateChairInput = (chairInput: string) => {
        this.setState((state) => ({ ...state, chairInput }));
        this.props.updateSelectedChairNumber(
            instanciateNominalNumber<ChairNumber>(parseInt(chairInput))
        );
    };

    private savePlan = () => {
        if (this.state.isSaving) {
            return;
        }
        this.setState((state) => ({ ...state, isSaving: true }));
        this.props.savePlan(() =>
            this.setState((state) => ({ ...state, isSaving: false }))
        );
    };

    private archive = () => {
        if (this.state.isArchiving) {
            return;
        }
        if (!this.props.archive) {
            throw Errors._62651552();
        }
        this.setState((state) => ({ ...state, isArchiving: true }));
        this.props.archive(() =>
            this.setState((state) => ({ ...state, isArchiving: false }))
        );
    };

    public render() {
        const autoIncrementCheckboxId = domUid();
        const hideAssociationsCheckboxId = domUid();
        return (
            <div
                css={css`
                    padding: 5px 0;
                    height: ${inputComponentHeight}px;
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
                            selection={this.props.selection}
                            elus={this.props.elus}
                            eluById={this.props.eluById}
                            groupePolitiqueById={this.props.groupePolitiqueById}
                            updateSelectedEluId={this.props.updateSelectedEluId}
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
                            onClick={this.savePlan}
                        >
                            {!this.state.isSaving && (
                                <React.Fragment>Enregistrer</React.Fragment>
                            )}
                            {this.state.isSaving && (
                                <LoadingIcon
                                    height={20}
                                    color={colors.blueborder}
                                />
                            )}
                        </div>
                    </div>
                    {this.props.archive && (
                        <div
                            css={css`
                                flex: 2;
                                padding-left: 4px;
                            `}
                        >
                            <div
                                css={css`
                                    background: ${colors.clearGrey};
                                    border: 1px solid ${colors.grey};
                                    color: ${colors.grey};
                                    height: 36px;
                                    margin: 2px;
                                    border-radius: 2px;
                                    padding-top: 9px;
                                    text-align: center;
                                    font-size: 12px;
                                    cursor: pointer;
                                    font-weight: bold;
                                `}
                                onClick={this.archive}
                            >
                                {!this.state.isArchiving && (
                                    <React.Fragment>Archiver</React.Fragment>
                                )}
                                {this.state.isArchiving && (
                                    <LoadingIcon
                                        height={20}
                                        color={colors.grey}
                                    />
                                )}
                            </div>
                        </div>
                    )}
                    <PrintButton print={this.props.print} />
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
