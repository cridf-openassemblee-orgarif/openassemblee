/** @jsx jsx */
import { css, jsx } from '@emotion/core';
import * as React from 'react';
import { colors } from '../../constants';
import { SelectedEluSource } from '../App';
import EluComponent from './EluComponent';
import { Elu, GroupePolitique } from '../../domain/elu';
import {
    ChairNumber,
    EluId,
    GroupePolitiqueId,
    numberifyNominalNumber,
} from '../../domain/nominal';
import { Dict, get, getOrNull } from '../../utils';
import { Association } from '../../domain/hemicycle';

interface Props {
    groupePolitiqueNomCourt: string;
    groupePolitiqueCouleur: string;
    elus: Elu[];
    associationByEluId: Dict<EluId, Association>;
    hideAssociations: boolean;
    selectedEluId?: EluId;
    updateSelectedEluId: (
        selectedEluId: EluId | undefined,
        source: SelectedEluSource
    ) => void;
    removeAssociation: (chair: ChairNumber) => void;
    deleteMode: boolean;
}

export default class GroupePolitiqueComponent extends React.PureComponent<
    Props
> {
    public render() {
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
                ({ chairNumber }) =>
                    !this.props.hideAssociations || !chairNumber
            );
        if (elusWithChairNumbers.length === 0) {
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
                        background: ${this.props.groupePolitiqueCouleur};
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
                        {this.props.groupePolitiqueNomCourt}
                    </span>
                </div>
                <div
                    css={css`
                        border: 4px solid ${this.props.groupePolitiqueCouleur};
                        border-top: 0;
                        padding: 10px;
                    `}
                >
                    {elusWithChairNumbers.map(({ elu, chairNumber }) => (
                        <EluComponent
                            key={numberifyNominalNumber(elu.id)}
                            elu={elu}
                            chairNumber={chairNumber}
                            isSelected={this.props.selectedEluId === elu.id}
                            deleteMode={this.props.deleteMode}
                            updateSelectedEluId={this.props.updateSelectedEluId}
                            removeAssociation={this.props.removeAssociation}
                        />
                    ))}
                </div>
            </div>
        );
    }
}
