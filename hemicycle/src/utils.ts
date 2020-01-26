import { css } from '@emotion/core';

export const clearfix = css`
    &:after {
        content: '';
        display: table;
        clear: both;
    }
`;

let uniqueIdIndex = 0;
export const domUid = () => 'WivaldyUid_' + uniqueIdIndex++;

export const eluToString = (elu: Elu) => elu.prenom + ' ' + elu.nom;
