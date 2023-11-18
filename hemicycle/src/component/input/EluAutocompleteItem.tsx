/** @jsx jsx */
import { css, jsx } from '@emotion/core';
import * as React from 'react';
import { colors, hexToRgbA } from '../../constants';
import { Elu, GroupePolitique } from '../../domain/elu';
import { GroupePolitiqueId } from '../../domain/nominal';
import { Dict, get } from '../../utils';

interface Props {
    elu: Elu;
    highlighted: boolean;
    groupePolitiqueById: Dict<GroupePolitiqueId, GroupePolitique>;
}

export default class EluAutocompleteItem extends React.PureComponent<Props> {
    public render() {
        const elu = this.props.elu;
        const groupePolitique = elu.groupePolitiqueId
            ? get(this.props.groupePolitiqueById, elu.groupePolitiqueId)
            : undefined;
        const couleurGroupePolitique = groupePolitique
            ? groupePolitique.couleur
            : '#000';
        return (
            <div
                css={css`
                    background: ${this.props.highlighted
                        ? colors.blue
                        : colors.white};
                    margin: 2px 0;
                `}
            >
                <div
                    css={css`
                        border: 4px solid ${couleurGroupePolitique};
                        background: ${hexToRgbA(couleurGroupePolitique, 0.15)};
                        border-top: 0;
                        border-bottom: 0;
                        padding: 10px;
                    `}
                >
                    <div
                        css={css`
                            font-size: 16px;
                            padding: 2px 10px;
                            cursor: pointer;
                        `}
                    >
                        {elu.prenom} {elu.nom}
                    </div>
                </div>
            </div>
        );
    }
}
