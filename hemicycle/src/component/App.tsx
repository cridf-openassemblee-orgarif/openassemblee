/** @jsx jsx */
import { css, jsx } from '@emotion/core';
import React from 'react';
import { injector } from '../service/injector';
import { clearfix } from '../utils';
import SizingContainer from './SizingContainer';
import Hemicycle from './Hemicycle';
import EluListComponent from './EluListComponent';
import InputsComponent from './InputsComponent';
import { colors } from '../constants';

const nonGroupePolitiqueId = -1;

const convertElu = (dto: EluListDTO): Elu => ({
    id: dto.elu.id,
    civilite: dto.elu.civilite,
    nom: dto.elu.nom,
    prenom: dto.elu.prenom,
    groupePolitiqueId: dto.groupePolitique
        ? dto.groupePolitique.id
        : nonGroupePolitiqueId
});

const convertGroupePolitique = (
    gp: GroupePolitiqueFromWs
): GroupePolitique => ({
    id: gp.id,
    nom: gp.nom,
    nomCourt: gp.nomCourt,
    couleur: '#' + gp.couleur
});

const alphabeticSort = (map: (item: any) => string) => (
    first: any,
    second: any
) => {
    const a = map(first);
    const b = map(second);
    if (a > b) {
        return -1;
    }
    if (b > a) {
        return 1;
    }
    return 0;
};

export interface Association {
    // FIXMENOW chairNumber
    chair: number;
    elu: Elu;
}

export interface Selections {
    selectedChairNumber?: number;
    selectedElu?: Elu;
    updateSelectedChairNumber: (selectedChairNumber: number) => void;
    updateSelectedElu: (selectedElu: Elu) => void;
    removeAssociation: (chair: number) => void;
}

export interface Associations {
    list: Association[];
    associationsByChair: Record<number, Association | undefined>;
    associationsByElu: Record<number, Association | undefined>;
}

export interface AppData {
    elus: Elu[];
    elusByGroupe: Record<number, Elu[]>;
    groupePolitiques: GroupePolitique[];
    groupePolitiquesById: Record<number, GroupePolitique>;
}

interface State {
    selectedChairNumber?: number;
    selectedElu?: Elu;
    associations: Associations;
    hemicycle?: HemicycleDTO;
    data?: AppData;
}

export default class App extends React.PureComponent<{}, State> {
    state: State = {
        selectedChairNumber: undefined,
        selectedElu: undefined,
        hemicycle: undefined,
        data: undefined,
        associations: {
            list: [],
            associationsByChair: {},
            associationsByElu: {}
        }
    };

    componentDidMount(): void {
        injector()
            .httpService.get(injector().urlBase + '/api/hemicycle')
            .then(a => {
                this.setState(state => ({
                    ...state,
                    hemicycle: a.body
                }));
            });
        injector()
            .httpService.get(injector().urlBase + '/api/elus')
            .then(a => {
                const elus: Elu[] = [];
                const elusByGroupe: Record<number, Elu[]> = {};
                const gps: GroupePolitique[] = [];
                const groupePolitiquesById: Record<
                    number,
                    GroupePolitique
                > = {};

                const eluListDTOs = a.body as EluListDTO[];
                eluListDTOs.forEach(d => {
                    const elu = convertElu(d);
                    elus.push(elu);

                    const groupePolitiqueId = d.groupePolitique
                        ? d.groupePolitique.id
                        : nonGroupePolitiqueId;
                    let groupeElus = elusByGroupe[groupePolitiqueId];
                    if (!groupeElus) {
                        groupeElus = [];
                        elusByGroupe[groupePolitiqueId] = groupeElus;
                    }
                    groupeElus.push(elu);

                    if (
                        d.groupePolitique &&
                        !Object.keys(groupePolitiquesById).includes(
                            d.groupePolitique.id.toString()
                        )
                    ) {
                        const groupePolitique = convertGroupePolitique(
                            d.groupePolitique
                        );
                        gps.push(groupePolitique);
                        groupePolitiquesById[
                            d.groupePolitique.id
                        ] = groupePolitique;
                    }
                });
                Object.keys(elusByGroupe).forEach((idAsString: string) => {
                    const id = parseInt(idAsString);
                    elusByGroupe[id] = elusByGroupe[id].sort(
                        alphabeticSort((elu: Elu) => elu.nom)
                    );
                });
                const groupePolitiques = gps.sort(
                    alphabeticSort((gp: GroupePolitique) => gp.nom)
                );
                // FIXMENOW doc : potentiellement relou mais pas censé avoir d'élu sans groupe !
                // ou reprendre les couleurs du groupe "sans groupe" effectif
                // dans la conf de l'hemicycle =)))
                // et redescendre le groupe avec l'hemicycle pour être sur de l'avoir
                const nonGroupe: GroupePolitique = {
                    id: nonGroupePolitiqueId,
                    nom: 'Sans groupe',
                    nomCourt: 'Sans groupe',
                    couleur: colors.black
                };
                groupePolitiques.unshift(nonGroupe);
                groupePolitiquesById[nonGroupePolitiqueId] = nonGroupe;
                this.setState(state => ({
                    ...state,
                    data: {
                        elus: elus.sort(alphabeticSort((elu: Elu) => elu.nom)),
                        groupePolitiques,
                        elusByGroupe,
                        groupePolitiquesById
                    }
                }));
            });
    }

    private associationsCollections = (
        list: Association[]
    ): { associations: Associations } => {
        const associationsByChair: Record<number, Association | undefined> = {};
        const associationsByElu: Record<number, Association | undefined> = {};
        list.forEach(a => {
            associationsByChair[a.chair] = a;
            associationsByElu[a.elu.id] = a;
        });
        return {
            associations: {
                list,
                associationsByChair,
                associationsByElu
            }
        };
    };

    private checkSelections = () => {
        this.setState(state => {
            const selectedChairNumber = state.selectedChairNumber;
            const selectedElu = state.selectedElu;
            if (selectedChairNumber && selectedElu) {
                const newAssociation: Association = {
                    chair: selectedChairNumber,
                    elu: selectedElu
                };
                const newAssociations = state.associations.list.filter(
                    a =>
                        a.chair !== selectedChairNumber &&
                        a.elu.id !== selectedElu.id
                );
                newAssociations.push(newAssociation);
                return {
                    ...state,
                    selectedChairNumber: undefined,
                    selectedElu: undefined,
                    ...this.associationsCollections(newAssociations)
                };
            } else {
                return state;
            }
        });
    };

    private updateSelectedChairNumber = (selectedChairNumber: number) => {
        this.setState(state => ({
            ...state,
            selectedChairNumber
        }));
        this.checkSelections();
    };

    private updateSelectedElu = (selectedElu: Elu) => {
        this.setState(state => ({ ...state, selectedElu }));
        this.checkSelections();
    };

    private removeAssociation = (chair: number) => {
        this.setState(state => {
            const newAssociations = state.associations.list.filter(
                a => a.chair !== chair
            );
            return {
                ...state,
                ...this.associationsCollections(newAssociations)
            };
        });
    };

    public render() {
        const selections: Selections = {
            selectedChairNumber: this.state.selectedChairNumber,
            selectedElu: this.state.selectedElu,
            updateSelectedChairNumber: this.updateSelectedChairNumber,
            updateSelectedElu: this.updateSelectedElu,
            removeAssociation: this.removeAssociation
        };
        return (
            <SizingContainer
                render={(width: number, height: number) => (
                    <div
                        css={css`
                            ${clearfix};
                            width: 100%;
                            height: ${height}px;
                        `}
                    >
                        <div
                            css={css`
                                float: left;
                                width: ${(width * 3) / 4}px;
                                height: ${height}px;
                            `}
                        >
                            {this.state.data && (
                                <div
                                    css={css`
                                        width: 40%;
                                        margin: auto;
                                    `}
                                >
                                    <InputsComponent
                                        data={this.state.data}
                                        selections={selections}
                                    />
                                </div>
                            )}
                            {this.state.hemicycle && this.state.data && (
                                <Hemicycle
                                    width={(width * 3) / 4}
                                    height={height}
                                    hemicycle={this.state.hemicycle}
                                    data={this.state.data}
                                    selections={selections}
                                    associations={this.state.associations}
                                />
                            )}
                        </div>
                        <div
                            css={css`
                                float: left;
                                width: ${width / 4}px;
                                height: ${height}px;
                            `}
                        >
                            {this.state.data && (
                                <EluListComponent
                                    data={this.state.data}
                                    selections={selections}
                                    associations={this.state.associations}
                                />
                            )}
                        </div>
                    </div>
                )}
            />
        );
    }
}
