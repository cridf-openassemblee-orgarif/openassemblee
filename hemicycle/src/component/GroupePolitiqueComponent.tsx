/** @jsx jsx */
import { css, jsx } from '@emotion/core';
import React from 'react';
import { colors } from '../constants';
import { AppData, Associations, Selections } from './App';

interface Props {
    groupePolitique: GroupePolitique;
    data: AppData;
    displayAssociations: boolean;
    associations: Associations;
    selections: Selections;
}

export default class GroupePolitiqueComponent extends React.PureComponent<
    Props
> {
    public render() {
        let elus = this.props.data.elusByGroupe[this.props.groupePolitique.id];
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
                    {elus
                        .map((elu: Elu) => ({
                            elu,
                            association: this.props.associations
                                .associationsByElu[elu.id]
                        }))
                        .filter(
                            ({ elu, association }) =>
                                this.props.displayAssociations || !association
                        )
                        .map(({ elu, association }) => (
                            <div
                                key={elu.id}
                                css={css`
                                    position: relative;
                                    font-size: 16px;
                                    padding: 2px 10px;
                                    cursor: pointer;
                                    ${this.props.selections.selectedElu?.id ===
                                    elu.id
                                        ? css`
                                              background: ${colors.blue};
                                          `
                                        : css`
                                              &:hover {
                                                  background: ${colors.clearGrey};
                                              }
                                          `}
                                `}
                                onClick={() =>
                                    this.props.selections.updateSelectedElu(elu, 'list')
                                }
                            >
                                <span
                                    css={css`
                                        display: inline-block;
                                        width: 40px;
                                    `}
                                >
                                    {association?.chair}
                                </span>
                                {elu.prenom} {elu.nom}
                                {association?.chair && (
                                    <div
                                        css={css`
                                            position: absolute;
                                            top: 2px;
                                            right: 2px;
                                        `}
                                        onClick={e => {
                                            this.props.selections.removeAssociation(
                                                association.chair
                                            );
                                            e.stopPropagation();
                                        }}
                                    >
                                        ✕
                                    </div>
                                )}
                            </div>
                        ))}
                </div>
            </div>
        );
    }
}
