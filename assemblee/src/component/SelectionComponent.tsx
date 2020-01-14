/** @jsx jsx */
import { css, jsx } from '@emotion/core';
import React from 'react';
import GroupePolitiqueComponent from './GroupePolitiqueComponent';

interface Props {
    eluListDTOs: EluListDTO[];
}

export default class SelectionComponent extends React.PureComponent<Props> {
    public render() {
        const elusParGroupe = {} as any;
        const groupes = {} as any;
        this.props.eluListDTOs.forEach(e => {
            if (e.groupePolitique) {
                if (!elusParGroupe[e.groupePolitique.id]) {
                    elusParGroupe[e.groupePolitique.id] = [];
                }
                elusParGroupe[e.groupePolitique.id].push(e.elu);
                groupes[e.groupePolitique.id] = e.groupePolitique;
                // } else {
                //     if (!elusParGroupe[sansGroupePolitique]) {
                //         elusParGroupe[sansGroupePolitique] = [];
                //     }
                //     elusParGroupe[sansGroupePolitique].push(e.elu);
                //     groupes[sansGroupePolitique] = {
                //         nomCourt: 'Sans',
                //         couleur: colors.greyWithoutSharp
                //     } as Partial<GroupePolitique>;
            }
        });
        return (
            <div
                css={css`
                    //padding: 10px;
                    //margin-right: 10px;
                    position: relative;
                    height: 100%;
                    overflow: scroll;
                `}
            >
                {Object.keys(elusParGroupe).map(gp => {
                    const groupe = groupes[gp];
                    if (!groupe) {
                        throw new Error();
                    }
                    const elus = elusParGroupe[gp];
                    if (!elus) {
                        throw new Error();
                    }
                    return (
                        <GroupePolitiqueComponent
                            key={groupe.id}
                            groupePolitique={groupe}
                            elus={elus}
                        />
                    );
                })}
            </div>
        );
    }
}
