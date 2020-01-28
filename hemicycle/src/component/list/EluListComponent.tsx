/** @jsx jsx */
import { css, jsx } from '@emotion/core';
import React from 'react';
import GroupePolitiqueComponent from './GroupePolitiqueComponent';
import { domUid } from '../../utils';
import { AppData, Associations, SelectedEluSource } from '../App';
import { colors } from '../../constants';

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

interface State {
    hideAssociations: boolean;
}

export default class EluListComponent extends React.Component<Props, State> {
    state = {
        hideAssociations: false
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
                <label
                    htmlFor={checkboxId}
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
                        id={checkboxId}
                        checked={this.state.hideAssociations}
                        onChange={this.switchDisplayAssociations}
                        disabled={this.props.deleteMode}
                    />{' '}
                    Cacher les associations
                </label>
                {this.props.data.groupePolitiques.map(groupePolitique => {
                    return (
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
                    );
                })}
            </div>
        );
    }
}
