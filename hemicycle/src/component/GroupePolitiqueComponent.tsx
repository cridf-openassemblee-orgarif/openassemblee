/** @jsx jsx */
import { css, jsx } from '@emotion/core';
import React from 'react';
import { colors } from '../constants';
import { AppData, Associations, SelectedEluSource } from './App';
import { clearfix } from '../utils';

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

const chairNumberWidth = 40;

export default class GroupePolitiqueComponent extends React.PureComponent<
    Props
> {
    public render() {
        let eluAssociations = this.props.data.elusByGroupe[
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
                        background: ${this.props.groupePolitique.couleur};
                        padding-left: 20px;
                    `}
                >
                    <span
                        css={css`
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
                        <div
                            key={elu.id}
                            css={css`
                                position: relative;
                                font-size: 14px;
                                padding: 2px 10px;
                                cursor: pointer;
                                ${this.props.selectedElu?.id === elu.id
                                    ? css`
                                          background: ${colors.blue};
                                      `
                                    : css`
                                          &:hover {
                                              background: ${colors.clearGrey};
                                          }
                                      `}
                                ${!association &&
                                    this.props.deleteMode &&
                                    css`
                                        color: ${colors.grey};
                                        cursor: auto;
                                        &:hover {
                                            background: ${colors.white};
                                        }
                                    `}
                            `}
                            onClick={() => {
                                if (!this.props.deleteMode) {
                                    this.props.updateSelectedElu(elu, 'list');
                                } else {
                                    if (association) {
                                        this.props.removeAssociation(
                                            association.chair
                                        );
                                    }
                                }
                            }}
                        >
                            <div
                                css={css`
                                    position: absolute;
                                    width: ${chairNumberWidth}px;
                                    text-align: right;
                                    padding-right: 20px;
                                `}
                            >
                                {association?.chair}
                            </div>
                            <div
                                css={css`
                                    padding-left: ${chairNumberWidth}px;
                                `}
                            >
                                {elu.prenom} {elu.nom}
                                {association?.chair && !this.props.deleteMode && (
                                    <div
                                        css={css`
                                            position: absolute;
                                            top: 2px;
                                            right: 2px;
                                        `}
                                        onClick={e => {
                                            this.props.removeAssociation(
                                                association.chair
                                            );
                                            e.stopPropagation();
                                        }}
                                    >
                                        ✕
                                    </div>
                                )}
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        );
    }
}
