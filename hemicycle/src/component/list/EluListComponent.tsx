/** @jsx jsx */
import { css, jsx } from '@emotion/core';
import * as React from 'react';
import GroupePolitiqueComponent from './GroupePolitiqueComponent';
import { Dict, domUid } from '../../utils';
import EluAlphabeticalListComponent from './EluAlphabeticalListComponent';
import LastAssociationsComponent from './LastAssociationsComponent';
import {
    ChairNumber,
    EluId,
    GroupePolitiqueId,
    numberifyNominalNumber
} from '../../domain/nominal';
import { Elu, GroupePolitique } from '../../domain/elu';
import { Association } from '../../domain/hemicycle';
import { SelectedEluSource } from '../App';

interface Props {
    eluById: Dict<EluId, Elu>;
    groupePolitiques: GroupePolitique[];
    elusByGroupeId: Dict<GroupePolitiqueId, Elu[]>;
    elus: Elu[];
    associationByEluId: Dict<EluId, Association>;
    associations: Association[];
    selectedEluId?: EluId;
    updateSelectedEluId: (
        selectedEluId: EluId | undefined,
        source: SelectedEluSource
    ) => void;
    removeAssociation: (chair: ChairNumber) => void;
    deleteMode: boolean;
}

type DisplayBy = 'groupePolitique' | 'elu';

interface State {
    hideAssociations: boolean;
    displayBy: DisplayBy;
}

export default class EluListComponent extends React.Component<Props, State> {
    public state: State = {
        hideAssociations: false,
        displayBy: 'groupePolitique'
    };

    componentDidUpdate(
        prevProps: Readonly<Props>,
        prevState: Readonly<State>,
        snapshot?: any
    ): void {
        if (!prevProps.deleteMode && this.props.deleteMode) {
            this.setState(state => ({
                ...state,
                hideAssociations: false
            }));
        }
    }

    private switchDisplayAssociations = () =>
        this.setState(state => ({
            ...state,
            hideAssociations: !state.hideAssociations
        }));

    private selectDisplay = (event: React.ChangeEvent<HTMLSelectElement>) => {
        const value = event.target
            ? (event.target.value as DisplayBy)
            : undefined;
        if (value) {
            this.setState(state => ({
                ...state,
                displayBy: value
            }));
        }
    };

    render() {
        const checkboxId = domUid();
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
                <LastAssociationsComponent
                    eluById={this.props.eluById}
                    selectedEluId={this.props.selectedEluId}
                    associations={this.props.associations}
                    deleteMode={this.props.deleteMode}
                    updateSelectedEluId={this.props.updateSelectedEluId}
                    removeAssociation={this.props.removeAssociation}
                />
                <div
                    css={css`
                        padding-top: 6px;
                    `}
                >
                    Liste des élus par :{' '}
                    <select
                        css={css`
                            font-size: 16px;
                        `}
                        onChange={this.selectDisplay}
                        value={this.state.displayBy}
                    >
                        <option value="groupePolitique">
                            Groupe politique
                        </option>
                        <option value="elu">Élu</option>
                    </select>
                </div>
                <label htmlFor={checkboxId}>
                    <input
                        type="checkbox"
                        id={checkboxId}
                        checked={this.state.hideAssociations}
                        onChange={this.switchDisplayAssociations}
                    />{' '}
                    Cacher les associations
                </label>
                {this.state.displayBy === 'groupePolitique' &&
                    this.props.groupePolitiques.map(gp => (
                        <GroupePolitiqueComponent
                            key={numberifyNominalNumber(gp.id)}
                            groupePolitique={gp}
                            elusByGroupeId={this.props.elusByGroupeId}
                            associationByEluId={this.props.associationByEluId}
                            hideAssociations={this.state.hideAssociations}
                            selectedEluId={this.props.selectedEluId}
                            updateSelectedEluId={this.props.updateSelectedEluId}
                            removeAssociation={this.props.removeAssociation}
                            deleteMode={this.props.deleteMode}
                        />
                    ))}
                {this.state.displayBy === 'elu' && (
                    <EluAlphabeticalListComponent
                        elus={this.props.elus}
                        associationByEluId={this.props.associationByEluId}
                        selectedEluId={this.props.selectedEluId}
                        updateSelectedEluId={this.props.updateSelectedEluId}
                        removeAssociation={this.props.removeAssociation}
                        deleteMode={this.props.deleteMode}
                        hideAssociations={this.state.hideAssociations}
                    />
                )}
            </div>
        );
    }
}
