/** @jsx jsx */
import { css, jsx } from '@emotion/core';
import React from 'react';
import { colors } from '../constants';

interface Props {
    eluListDTOs: EluListDTO[];
}

export default class SelectionComponent extends React.PureComponent<Props> {
    public render() {
        return (
            <div
                css={css`
                    background: ${colors.grey};
                    padding: 10px;
                `}
            >
                {this.props.eluListDTOs.map(e => (
                    <div key={e.elu.id}>
                        {e.elu.prenom} {e.elu.nom}
                    </div>
                ))}
            </div>
        );
    }
}
