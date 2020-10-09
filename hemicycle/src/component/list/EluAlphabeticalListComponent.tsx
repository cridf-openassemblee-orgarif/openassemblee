/** @jsx jsx */
import { css, jsx } from '@emotion/core';
import * as React from 'react';
import { SelectedEluSource } from '../App';
import { colors } from '../../constants';
import EluComponent from './EluComponent';
import _ from 'lodash';
import {
    ChairNumber,
    EluId,
    numberifyNominalNumber,
} from '../../domain/nominal';
import { Dict, getOrNull } from '../../utils';
import { Association } from '../../domain/hemicycle';
import { Elu } from '../../domain/elu';

interface Props {
    elus: Elu[];
    associationByEluId: Dict<EluId, Association>;
    selectedEluId?: EluId;
    updateSelectedEluId: (
        selectedEluId: EluId | undefined,
        source: SelectedEluSource
    ) => void;
    removeAssociation: (chair: ChairNumber) => void;
    deleteMode: boolean;
    hideAssociations: boolean;
    hideDemissionnaires: boolean;
}

export default class EluAlphabeticalListComponent extends React.Component<
    Props
> {
    render() {
        const elusWithChairNumbers: {
            elu: Elu;
            chairNumber: ChairNumber | undefined;
        }[] = this.props.elus
            .map((elu: Elu) => ({
                elu,
                chairNumber: getOrNull(this.props.associationByEluId, elu.id)
                    ?.chairNumber,
            }))
            .filter(
                ({ elu, chairNumber }) =>
                    (!this.props.hideAssociations || !chairNumber) &&
                    (!this.props.hideDemissionnaires || elu.actif)
            );
        const byFirstLetter = _.groupBy(
            elusWithChairNumbers,
            (a) => a.elu.nom[0]
        );
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
                    .map((firstLetter) => (
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
                                        (a) => a.elu.nom
                                    ).map(({ elu, chairNumber }) => (
                                        <EluComponent
                                            key={numberifyNominalNumber(elu.id)}
                                            elu={elu}
                                            chairNumber={chairNumber}
                                            isSelected={
                                                this.props.selectedEluId ===
                                                elu.id
                                            }
                                            deleteMode={this.props.deleteMode}
                                            updateSelectedEluId={
                                                this.props.updateSelectedEluId
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
