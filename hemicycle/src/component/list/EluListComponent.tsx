/** @jsx jsx */
import { css, jsx } from '@emotion/core';
import React from 'react';
import GroupePolitiqueComponent from './GroupePolitiqueComponent';
import { domUid } from '../../utils';
import { AppData, Associations, SelectedEluSource } from '../App';
import { colors } from '../../constants';
import EluAlphabeticalListComponent from './EluAlphabeticalListComponent';
import LastAssociationsComponent from './LastAssociationsComponent';

interface Props {
    selectedElu?: Elu;
    updateSelectedElu: (
        selectedElu: Elu | undefined,
        source: SelectedEluSource
    ) => void;
    removeAssociation: (chair: number) => void;
    associations: Associations;
    data: AppData;
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
                    associations={this.props.associations}
                    deleteMode={this.props.deleteMode}
                    updateSelectedElu={this.props.updateSelectedElu}
                    removeAssociation={this.props.removeAssociation}
                    selectedElu={this.props.selectedElu}
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
                    this.props.data.groupePolitiques.map(groupePolitique => (
                        <GroupePolitiqueComponent
                            key={groupePolitique.id}
                            groupePolitique={groupePolitique}
                            hideAssociations={this.state.hideAssociations}
                            associations={this.props.associations}
                            selectedElu={this.props.selectedElu}
                            updateSelectedElu={this.props.updateSelectedElu}
                            removeAssociation={this.props.removeAssociation}
                            data={this.props.data}
                            deleteMode={this.props.deleteMode}
                        />
                    ))}
                {this.state.displayBy === 'elu' && (
                    <EluAlphabeticalListComponent
                        data={this.props.data}
                        associations={this.props.associations}
                        selectedElu={this.props.selectedElu}
                        updateSelectedElu={this.props.updateSelectedElu}
                        removeAssociation={this.props.removeAssociation}
                        deleteMode={this.props.deleteMode}
                        hideAssociations={this.state.hideAssociations}
                    />
                )}
            </div>
        );
    }
}
