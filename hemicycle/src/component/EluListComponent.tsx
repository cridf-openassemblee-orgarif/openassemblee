/** @jsx jsx */
import { css, jsx } from '@emotion/core';
import React from 'react';
import GroupePolitiqueComponent from './GroupePolitiqueComponent';
import { domUid } from '../utils';
import { AppData, Associations, SelectedEluSource } from './App';

interface Props {
    selectedElu?: Elu;
    updateSelectedElu: (
        selectedElu: Elu | undefined,
        source: SelectedEluSource
    ) => void;
    removeAssociation: (chair: number) => void;
    associations: Associations;
    data: AppData;
}

interface State {
    displayAssociations: boolean;
}

export default class EluListComponent extends React.Component<Props, State> {
    state = {
        displayAssociations: true
    };

    private switchDisplayAssociations = () =>
        this.setState(state => ({
            ...state,
            displayAssociations: !state.displayAssociations
        }));

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
                <label htmlFor={checkboxId}>
                    <input
                        type="checkbox"
                        id={checkboxId}
                        value={this.state.displayAssociations.toString()}
                        onChange={this.switchDisplayAssociations}
                    />
                    Cacher les associations
                </label>
                {this.props.data.groupePolitiques.map(groupePolitique => {
                    return (
                        <GroupePolitiqueComponent
                            key={groupePolitique.id}
                            groupePolitique={groupePolitique}
                            displayAssociations={this.state.displayAssociations}
                            associations={this.props.associations}
                            selectedElu={this.props.selectedElu}
                            updateSelectedElu={this.props.updateSelectedElu}
                            removeAssociation={this.props.removeAssociation}
                            data={this.props.data}
                        />
                    );
                })}
            </div>
        );
    }
}
