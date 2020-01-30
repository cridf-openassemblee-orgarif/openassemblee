/** @jsx jsx */
import { css, jsx } from '@emotion/core';
import React from 'react';
import { colors } from '../../constants';
import { AppData, Associations, SelectedEluSource } from '../App';
import EluComponent from './EluComponent';

interface Props {
    groupePolitique: GroupePolitique;
    data: AppData;
    hideAssociations: boolean;
    associations: Associations;
    selectedElu?: Elu;
    updateSelectedElu: (
        selectedElu: Elu | undefined,
        source: SelectedEluSource
    ) => void;
    removeAssociation: (chair: number) => void;
    deleteMode: boolean;
}

export default class GroupePolitiqueComponent extends React.PureComponent<
    Props
> {
    public render() {
        const eluAssociations = this.props.data.elusByGroupe[
            this.props.groupePolitique.id
        ]
            .map((elu: Elu) => ({
                elu,
                association: this.props.associations.associationsByElu[elu.id]
            }))
            .filter(
                ({ elu, association }) =>
                    !this.props.hideAssociations || !association
            );
        if (eluAssociations.length === 0) {
            return null;
        }
        return (
            <div
                css={css`
                    background: ${colors.white};
                    margin: 10px 0;
                `}
            >
                <div
                    css={css`
                        height: 18px;
                        background: ${this.props.groupePolitique.couleur};
                        padding-left: 20px;
                    `}
                >
                    <span
                        css={css`
                            display: inline-block;
                            height: 18px;
                            background: ${colors.white};
                            padding: 0 10px;
                        `}
                    >
                        {this.props.groupePolitique.nomCourt}
                    </span>
                </div>
                <div
                    css={css`
                        border: 4px solid ${this.props.groupePolitique.couleur};
                        border-top: 0;
                        padding: 10px;
                    `}
                >
                    {eluAssociations.map(({ elu, association }) => (
                        <EluComponent
                            key={elu.id}
                            elu={elu}
                            association={association}
                            isSelected={this.props.selectedElu?.id === elu.id}
                            deleteMode={this.props.deleteMode}
                            updateSelectedElu={this.props.updateSelectedElu}
                            removeAssociation={this.props.removeAssociation}
                        />
                    ))}
                </div>
            </div>
        );
    }
}
