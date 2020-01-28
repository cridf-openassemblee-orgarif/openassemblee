/** @jsx jsx */
import { css, jsx } from '@emotion/core';
import React from 'react';
import GroupePolitiqueComponent from './GroupePolitiqueComponent';
import { domUid } from '../../utils';
import { AppData, Associations, SelectedEluSource } from '../App';
import { colors } from '../../constants';
import EluComponent from './EluComponent';
import _ from 'lodash';

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
    hideAssociations: boolean;
}

export default class EluAlphabeticalListComponent extends React.Component<
    Props
> {
    render() {
        const eluAssociations = this.props.data.elus
            .map((elu: Elu) => ({
                elu,
                association: this.props.associations.associationsByElu[elu.id]
            }))
            .filter(
                ({ elu, association }) =>
                    !this.props.hideAssociations || !association
            );
        const byFirstLetter = _.groupBy(eluAssociations, a => a.elu.nom[0]);
        return (
            <div
                css={css`
                    background: ${colors.white};
                    border: 1px solid ${colors.black};
                    padding: 4px;
                    margin-top: 10px;
                `}
            >
                {Object.keys(byFirstLetter)
                    .sort()
                    .map(firstLetter => (
                        <div
                            key={firstLetter}
                            css={css`
                                position: relative;
                                // for scrollbar
                                margin-right: 10px;
                            `}
                        >
                            <div
                                css={css`
                                    display: flex;
                                `}
                            >
                                <div
                                    css={css`
                                        flex: 3;
                                    `}
                                >
                                    {_.sortBy(
                                        byFirstLetter[firstLetter],
                                        a => a.elu.nom
                                    ).map(({ elu, association }) => (
                                        <EluComponent
                                            key={elu.id}
                                            elu={elu}
                                            association={association}
                                            isSelected={
                                                this.props.selectedElu?.id ===
                                                elu.id
                                            }
                                            deleteMode={this.props.deleteMode}
                                            updateSelectedElu={
                                                this.props.updateSelectedElu
                                            }
                                            removeAssociation={
                                                this.props.removeAssociation
                                            }
                                        />
                                    ))}
                                </div>
                                <div
                                    css={css`
                                        position: relative;
                                        top: -4px;
                                        width: 20px;
                                        font-weight: bold;
                                        font-size: 16px;
                                        // for tag hover effect
                                        margin-left: 10px;
                                    `}
                                >
                                    {firstLetter}
                                </div>
                            </div>
                        </div>
                    ))}
            </div>
        );
    }
}
