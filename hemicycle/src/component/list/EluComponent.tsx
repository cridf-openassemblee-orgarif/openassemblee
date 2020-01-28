/** @jsx jsx */
import { css, jsx } from '@emotion/core';
import { colors } from '../../constants';
import { Association, SelectedEluSource } from '../App';
import React from 'react';

interface Props {
    elu: Elu;
    association?: Association;
    isSelected: boolean;
    deleteMode: boolean;
    updateSelectedElu: (
        selectedElu: Elu | undefined,
        source: SelectedEluSource
    ) => void;
    removeAssociation: (chair: number) => void;
}

const chairNumberWidth = 40;

export default class EluComponent extends React.PureComponent<Props> {
    public render() {
        const association = this.props.association;
        return (
            <div
                css={css`
                    position: relative;
                    font-size: 14px;
                    padding: 2px 10px;
                    cursor: pointer;
                    ${this.props.isSelected
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
                        this.props.updateSelectedElu(this.props.elu, 'list');
                    } else {
                        if (association) {
                            this.props.removeAssociation(association.chair);
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
                    {this.props.elu.prenom} {this.props.elu.nom}
                    {association?.chair && !this.props.deleteMode && (
                        <div
                            css={css`
                                position: absolute;
                                top: 2px;
                                right: 2px;
                            `}
                            onClick={e => {
                                this.props.removeAssociation(association.chair);
                                e.stopPropagation();
                            }}
                        >
                            ✕
                        </div>
                    )}
                </div>
            </div>
        );
    }
}
