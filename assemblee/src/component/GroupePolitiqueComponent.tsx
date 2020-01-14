/** @jsx jsx */
import { css, jsx } from '@emotion/core';
import React from 'react';
import { colors } from '../constants';

interface Props {
    groupePolitique: GroupePolitique;
    elus: Elu[];
}

export default class GroupePolitiqueComponent extends React.PureComponent<
    Props
> {
    public render() {
        const { groupePolitique, elus } = this.props;
        return (
            <div
                css={css`
                    background: ${colors.white};
                    margin: 10px 0;
                `}
            >
                <div
                    css={css`
                        background: #${groupePolitique.couleur};
                        padding-left: 20px;
                    `}
                >
                    <span
                        css={css`
                            background: ${colors.white};
                            padding: 0 10px;
                        `}
                    >
                        {groupePolitique.nomCourt}
                    </span>
                </div>
                <div
                    css={css`
                        border: 4px solid #${groupePolitique.couleur};
                        border-top: 0;
                        padding: 10px;
                    `}
                >
                    {elus.map((e: Elu) => (
                        <div
                            key={e.id}
                            css={css`
                                font-size: 16px;
                            `}
                        >
                            {e.prenom} {e.nom}{' '}
                        </div>
                    ))}
                </div>
            </div>
        );
    }
}
