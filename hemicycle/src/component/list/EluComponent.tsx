/** @jsx jsx */
import { css, jsx } from '@emotion/core';
import { colors } from '../../constants';
import * as React from 'react';
import { Elu } from '../../domain/elu';
import { ChairNumber, EluId } from '../../domain/nominal';
import { SelectedEluSource } from '../App';

interface Props {
    elu: Elu;
    chairNumber?: ChairNumber;
    isSelected: boolean;
    deleteMode: boolean;
    updateSelectedEluId: (
        selectedEluId: EluId | undefined,
        source: SelectedEluSource
    ) => void;
    removeAssociation: (chair: ChairNumber) => void;
}

const chairNumberWidth = 40;

export default class EluComponent extends React.PureComponent<Props> {
    public render() {
        const chairNumber = this.props.chairNumber;
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
                    ${!chairNumber &&
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
                        this.props.updateSelectedEluId(
                            this.props.elu.id,
                            'list'
                        );
                    } else {
                        if (this.props.chairNumber) {
                            this.props.removeAssociation(
                                this.props.chairNumber
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
                    {chairNumber}
                </div>
                <div
                    css={css`
                        padding-left: ${chairNumberWidth}px;
                    `}
                >
                    {this.props.elu.prenom} {this.props.elu.nom}
                    {chairNumber && !this.props.deleteMode && (
                        <div
                            css={css`
                                position: absolute;
                                top: 2px;
                                right: 2px;
                            `}
                            onClick={e => {
                                this.props.removeAssociation(chairNumber);
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
