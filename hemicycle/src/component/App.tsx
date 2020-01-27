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
        return 1;
    }
    if (b > a) {
        return -1;
    }
    return 0;
};

export interface Association {
    // FIXMENOW chairNumber
    chair: number;
    elu: Elu;
}

export type SelectedEluSource = 'input' | 'list';

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
    selectedEluSource?: SelectedEluSource;
    associations: Associations;
    hemicycle?: HemicycleDTO;
    data?: AppData;
}

export default class App extends React.PureComponent<{}, State> {
    state: State = {
        selectedChairNumber: undefined,
        selectedElu: undefined,
        selectedEluSource: undefined,
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
                this.updateProtoAssociations(elus);
            });
    }

    private associationsCollections = (list: Association[]): Associations => {
        const associationsByChair: Record<number, Association | undefined> = {};
        const associationsByElu: Record<number, Association | undefined> = {};
        list.forEach(a => {
            associationsByChair[a.chair] = a;
            associationsByElu[a.elu.id] = a;
        });
        return {
            list,
            associationsByChair,
            associationsByElu
        };
    };

    private checkSelections = () => {
        // le fait de delayer ce set State permet à l'input de récupérer le selectedChairNumber via les props
        // pour le reprendre ensuite en undefined et détecter le changement effectif
        // est bugguy sans ça
        // + petit timeout non null permet une visualisation rapide en plus
        // risque de "bug" si clics très rapides, mais bon... => pas un bug catastrophique, un clic en remplace un autre
        // FIXMENOW éventuellement supprimer cette latence quand la saisie est dans l'input ? car ralentie la saisie
        // voire la dégager tout court en fait =]
        setTimeout(() => {
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
                        selectedEluSource: undefined,
                        associations: this.associationsCollections(
                            newAssociations
                        )
                    };
                } else {
                    return state;
                }
            });
        }, 200);
    };

    private updateSelectedChairNumber = (selectedChairNumber: number) => {
        this.setState(state => ({
            ...state,
            selectedChairNumber
        }));
        this.checkSelections();
    };

    private updateSelectedElu = (
        selectedElu: Elu | undefined,
        selectedEluSource: SelectedEluSource
    ) => {
        this.setState(state => ({ ...state, selectedElu, selectedEluSource }));
        this.checkSelections();
    };

    private removeAssociation = (chair: number) =>
        this.setState(state => {
            const newAssociations = state.associations.list.filter(
                a => a.chair !== chair
            );
            return {
                ...state,
                associations: this.associationsCollections(newAssociations)
            };
        });

    private protoAlphaSort = () => {
        this.setState(state => {
            const hemicycle = state.hemicycle;
            if (state.data && hemicycle) {
                const associations = state.data.elus.map(
                    (elu, index) =>
                        ({
                            chair: hemicycle.chairs[index].number,
                            elu: elu
                        } as Association)
                );
                return {
                    ...state,
                    associations: this.associationsCollections(associations)
                };
            } else {
                return state;
            }
        });
    };

    private updateProtoAssociations(elus: Elu[]) {
        injector()
            .httpService.get(injector().urlBase + '/api/proto-associations')
            .then(a => {
                const protoAssociations = a.body as [number, number][];
                const eluById = {} as Record<number, Elu>;
                elus.forEach(e => {
                    eluById[e.id] = e;
                });
                const associations: Association[] = protoAssociations.map(
                    a => ({
                        chair: a[0],
                        elu: eluById[a[1]]
                    })
                );
                this.setState(state => ({
                    ...state,
                    associations: this.associationsCollections(associations)
                }));
            });
    }

    private saveProtoAssociations = () => {
        const protoAssociations = this.state.associations.list.map(a => [
            a.chair,
            a.elu.id
        ]);
        injector().httpService.post(
            injector().urlBase + '/api/proto-associations',
            protoAssociations
        );
    };

    private protoEmpty = () =>
        this.setState(state => ({
            ...state,
            associations: this.associationsCollections([])
        }));

    public render() {
        return (
            <SizingContainer
                render={(width: number, height: number) => {
                    const hemicycleWidth = (5 * width) / 6;
                    const columnWidth = width - hemicycleWidth;
                    return (
                        <div
                            css={css`
                                ${clearfix};
                                width: 100%;
                                height: ${height}px;
                            `}
                        >
                            <div
                                css={css`
                                    position: relative;
                                    float: left;
                                    width: ${hemicycleWidth}px;
                                    height: ${height}px;
                                `}
                            >
                                {this.state.data && (
                                    <React.Fragment>
                                        <div
                                            css={css`
                                                position: absolute;
                                                top: 0;
                                                left: 0;
                                                background: ${colors.white};
                                                border: 1px solid ${colors.grey};
                                                padding: 4px;
                                            `}
                                        >
                                            <div
                                                css={css`
                                                    text-align: center;
                                                `}
                                            >
                                                [proto]
                                            </div>
                                            <button
                                                onClick={this.protoAlphaSort}
                                            >
                                                Ordre alphabétique
                                            </button>
                                            <button
                                                onClick={
                                                    this.saveProtoAssociations
                                                }
                                            >
                                                Enregistrer
                                            </button>
                                            <button onClick={this.protoEmpty}>
                                                Vider
                                            </button>
                                        </div>
                                        <div
                                            css={css`
                                                width: 40%;
                                                margin: auto;
                                            `}
                                        >
                                            <InputsComponent
                                                selectedChairNumber={
                                                    this.state
                                                        .selectedChairNumber
                                                }
                                                selectedElu={
                                                    this.state.selectedElu
                                                }
                                                selectedEluSource={
                                                    this.state.selectedEluSource
                                                }
                                                updateSelectedChairNumber={
                                                    this
                                                        .updateSelectedChairNumber
                                                }
                                                updateSelectedElu={
                                                    this.updateSelectedElu
                                                }
                                                data={this.state.data}
                                            />
                                        </div>
                                    </React.Fragment>
                                )}
                                {this.state.hemicycle && this.state.data && (
                                    <Hemicycle
                                        width={hemicycleWidth}
                                        height={height}
                                        hemicycle={this.state.hemicycle}
                                        data={this.state.data}
                                        associations={this.state.associations}
                                        selectedChairNumber={
                                            this.state.selectedChairNumber
                                        }
                                        updateSelectedChairNumber={
                                            this.updateSelectedChairNumber
                                        }
                                    />
                                )}
                            </div>
                            <div
                                css={css`
                                    float: left;
                                    width: ${columnWidth}px;
                                    height: ${height}px;
                                `}
                            >
                                {this.state.data && (
                                    <EluListComponent
                                        data={this.state.data}
                                        associations={this.state.associations}
                                        selectedElu={this.state.selectedElu}
                                        updateSelectedElu={
                                            this.updateSelectedElu
                                        }
                                        removeAssociation={
                                            this.removeAssociation
                                        }
                                    />
                                )}
                            </div>
                        </div>
                    );
                }}
            />
        );
    }
}
