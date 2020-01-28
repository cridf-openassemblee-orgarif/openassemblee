/** @jsx jsx */
import { css, jsx } from '@emotion/core';
import React from 'react';
import { colors } from '../../constants';
import { AppData } from '../App';

interface Props {
    elu: Elu;
    highlighted: boolean;
    data: AppData;
}

export default class EluAutocompleteItem extends React.PureComponent<Props> {
    public render() {
        const elu = this.props.elu;
        const groupePolitique = this.props.data.groupePolitiquesById[
            elu.groupePolitiqueId
        ];
        const couleurGroupePolitique = groupePolitique
            ? groupePolitique.couleur
            : '000';
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
