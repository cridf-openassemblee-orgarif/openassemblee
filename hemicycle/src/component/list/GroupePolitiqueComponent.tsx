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
    numberifyNominalNumber
} from '../../domain/nominal';
import { Dict, get, getMaybe } from '../../utils';
import { Association } from '../../domain/hemicycle';

interface Props {
    groupePolitique: GroupePolitique;
    elusByGroupeId: Dict<GroupePolitiqueId, Elu[]>;
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
        }[] = get(this.props.elusByGroupeId, this.props.groupePolitique.id)
            .map((elu: Elu) => ({
                elu,
                chairNumber: getMaybe(this.props.associationByEluId, elu.id)
                    ?.chairNumber
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
