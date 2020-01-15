/** @jsx jsx */
import { css, jsx } from '@emotion/core';
import React from 'react';
import { colors } from '../constants';

interface Props {
    groupePolitique: GroupePolitique;
    // FIXMENOW about ce naming
    eluDtos: EluListDTO[];
    // FIXME naming
    updateSelectedElu: (elu: EluListDTO) => void;
}

export default class GroupePolitiqueComponent extends React.PureComponent<
    Props
> {
    public render() {
        const { groupePolitique, eluDtos } = this.props;
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
                    {eluDtos.map((d: EluListDTO) => (
                        <div
                            key={d.elu.id}
                            css={css`
                                font-size: 16px;
                                padding: 2px 10px;
                                cursor: pointer;
                                &:hover {
                                    background: ${colors.clearGrey};
                                }
                            `}
                            onClick={() => this.props.updateSelectedElu(d)}
                        >
                            {d.elu.prenom} {d.elu.nom}
                        </div>
                    ))}
                </div>
            </div>
        );
    }
}
