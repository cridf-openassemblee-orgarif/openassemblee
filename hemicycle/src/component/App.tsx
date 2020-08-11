/** @jsx jsx */
import { css, jsx } from '@emotion/core';
import * as React from 'react';
import { injector } from '../service/injector';
import SizingContainer from './util/SizingContainer';
import Hemicycle from './Hemicycle';
import EluListComponent from './list/EluListComponent';
import InputsComponent, { inputComponentHeight } from './input/InputsComponent';
import { colors, options } from '../constants';
import { Unsuscriber } from '../service/EventBus';
import {
    Elu,
    EluListDTO,
    GroupePolitique,
    GroupePolitiqueFromWs,
} from '../domain/elu';
import {
    ChairNumber,
    EluId,
    GroupePolitiqueId,
    instanciateNominalNumber,
    PlanId,
} from '../domain/nominal';
import { Dict, get, set } from '../utils';
import { Errors } from './util/errors';
import {
    Association,
    HemicyclePlanAssociationsFromWs,
} from '../domain/hemicycle';
import { HemicycleConfigurationRendu } from '../domain/assemblee';
import LoadingIcon from './util/LoadingIcon';

const nonGroupePolitiqueId = instanciateNominalNumber<GroupePolitiqueId>(-1);

const convertElu = (dto: EluListDTO): Elu => ({
    id: dto.elu.id,
    civilite: dto.elu.civilite,
    nom: dto.elu.nom,
    prenom: dto.elu.prenom,
    groupePolitiqueId: dto.groupePolitique
        ? dto.groupePolitique.id
        : nonGroupePolitiqueId,
    shortFonction: dto.shortFonction,
});

const convertGroupePolitique = (
    gp: GroupePolitiqueFromWs
): GroupePolitique => ({
    id: gp.id,
    nom: gp.nom,
    nomCourt: gp.nomCourt,
    couleur: '#' + gp.couleur,
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

export type SelectedEluSource = 'input' | 'list';

interface Props {
    planId: PlanId;
    isProjet: boolean;
}

interface State {
    rawElus?: {
        elus: Elu[];
        groupePolitiques: GroupePolitique[];
    };
    hemicycle?: {
        associations: Association[];
        configurationRendu: HemicycleConfigurationRendu;
    };
    maps: {
        eluById?: Dict<EluId, Elu>;
        elusByGroupeId?: Dict<GroupePolitiqueId, Elu[]>;
        groupePolitiqueById?: Dict<GroupePolitiqueId, GroupePolitique>;
        associationByChair?: Dict<ChairNumber, Association>;
        associationByEluId?: Dict<EluId, Association>;
    };
    selection: Selection;
    config: {
        hideAssociationsChairs: boolean;
        deleteMode: boolean;
    };
}

export interface Selection {
    selectedChairNumber?: ChairNumber;
    selectedEluId?: EluId;
    selectedEluSource?: SelectedEluSource;
}

export default class App extends React.PureComponent<Props, State> {
    state: State = {
        rawElus: undefined,
        hemicycle: undefined,
        maps: {
            eluById: undefined,
            elusByGroupeId: undefined,
            groupePolitiqueById: undefined,
            associationByChair: undefined,
            associationByEluId: undefined,
        },
        selection: {
            selectedChairNumber: undefined,
            selectedEluId: undefined,
            selectedEluSource: undefined,
        },
        config: {
            hideAssociationsChairs: false,
            deleteMode: false,
        },
    };

    private unsuscriber: Unsuscriber | undefined;

    componentDidMount(): void {
        injector()
            .httpService.get(injector().urlBase + '/api/elus')
            .then((a) => {
                const eluDtos = a.body as EluListDTO[];
                const elus = eluDtos.map((d) => convertElu(d));
                const groupePolitiques = this.elusDtoToGroupePolitiques(
                    eluDtos
                );
                this.setState((state) => ({
                    ...state,
                    rawElus: {
                        ...state.rawElus,
                        elus,
                        groupePolitiques,
                    },
                }));
                this.updateElusMaps();
                this.updateAssociationsMaps();
            });
        injector()
            .httpService.get(
                injector().urlBase +
                    '/api/hemicyclePlans-associations/' +
                    this.props.planId
            )
            .then((a) => {
                const hemicycle = a.body as HemicyclePlanAssociationsFromWs;
                this.setState((state) => ({
                    ...state,
                    hemicycle,
                }));
                this.updateAssociationsMaps();
            });
        this.unsuscriber = injector().applicationEventBus.subscribe(
            'activate_debug',
            () => this.forceUpdate()
        );
    }

    componentWillUnmount() {
        if (this.unsuscriber) {
            this.unsuscriber();
        }
    }

    private elusDtoToGroupePolitiques = (
        eluDtos: EluListDTO[]
    ): GroupePolitique[] => {
        const gps: GroupePolitique[] = [];
        eluDtos
            .filter((d) => d.groupePolitique)
            .forEach((d) => {
                if (
                    gps.filter((gp) => gp.id === d.groupePolitique?.id)
                        .length === 0
                ) {
                    gps.push(convertGroupePolitique(d.groupePolitique!));
                }
            });
        // tri par ordre alphabétique
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
            couleur: colors.black,
        };
        groupePolitiques.unshift(nonGroupe);
        return groupePolitiques;
    };

    private updateElusMaps = () =>
        this.setState((state) => {
            const eluById: Dict<EluId, Elu> = {};
            const elusByGroupeId: Dict<GroupePolitiqueId, Elu[]> = {};
            const groupePolitiqueById: Dict<
                GroupePolitiqueId,
                GroupePolitique
            > = {};
            if (!state.rawElus) {
                throw Errors._b7d84f98();
            }
            state.rawElus.elus.forEach((e) => {
                set(eluById, e.id, e);
            });
            state.rawElus.groupePolitiques.forEach((gp) => {
                set(elusByGroupeId, gp.id, []);
                set(groupePolitiqueById, gp.id, gp);
            });
            // FIXMENOW filtrer les demissionaires ? ou et comment
            state.rawElus.elus.forEach((e) => {
                get(elusByGroupeId, e.groupePolitiqueId).push(e);
            });
            // elus dans ordre alphabétique
            Object.keys(elusByGroupeId).forEach((idAsString: string) => {
                const gpId = instanciateNominalNumber<GroupePolitiqueId>(
                    parseInt(idAsString)
                );
                set(
                    elusByGroupeId,
                    gpId,
                    get(elusByGroupeId, gpId).sort(
                        alphabeticSort((elu: Elu) => elu.nom)
                    )
                );
            });
            return {
                ...state,
                maps: {
                    ...state.maps,
                    eluById,
                    elusByGroupeId,
                    groupePolitiqueById,
                },
            };
        });

    private updateAssociationsMaps = () =>
        this.setState((state) => {
            if (!state.hemicycle) {
                return state;
            }
            const associationByChair: Dict<ChairNumber, Association> = {};
            const associationByEluId: Dict<EluId, Association> = {};
            state.hemicycle.associations.forEach((a) => {
                set(associationByChair, a.chairNumber, a);
                set(associationByEluId, a.eluId, a);
            });
            return {
                ...state,
                maps: {
                    ...state.maps,
                    associationByChair,
                    associationByEluId,
                },
            };
        });

    private selectionsToAssociation = () => {
        // le fait de delayer ce set State permet à l'input de récupérer le selectedChairNumber via les props
        // pour le reprendre ensuite en undefined et détecter le changement effectif
        // est bugguy sans ça
        // + petit timeout non null permet une visualisation rapide en plus
        // risque de "bug" si clics très rapides, mais bon... => pas un bug catastrophique, un clic en remplace un autre
        // FIXMENOW éventuellement supprimer cette latence quand la saisie est dans l'input ? car ralentie la saisie
        // voire la dégager tout court en fait =]
        setTimeout(() => {
            this.setState((state) => {
                const selectedChairNumber = state.selection.selectedChairNumber;
                const selectedEluId = state.selection.selectedEluId;
                if (selectedChairNumber && selectedEluId) {
                    if (!state.hemicycle) {
                        throw Errors._8459c73c();
                    }
                    const newAssociation: Association = {
                        chairNumber: selectedChairNumber,
                        eluId: selectedEluId,
                    };
                    const associations = state.hemicycle.associations.filter(
                        (a) =>
                            a.chairNumber !== selectedChairNumber &&
                            a.eluId !== selectedEluId
                    );
                    associations.push(newAssociation);
                    return {
                        ...state,
                        selection: {
                            ...state.selection,
                            selectedChairNumber: undefined,
                            selectedEluId: undefined,
                            selectedEluSource: undefined,
                        },
                        hemicycle: {
                            ...state.hemicycle,
                            associations,
                        },
                    };
                } else {
                    return state;
                }
            });
            this.updateAssociationsMaps();
        }, 200);
    };

    private updateSelectedChairNumber = (selectedChairNumber: ChairNumber) => {
        this.setState((state) => ({
            ...state,
            selection: {
                ...state.selection,
                selectedChairNumber,
            },
        }));
        this.selectionsToAssociation();
    };

    private updateSelectedEluId = (
        selectedEluId: EluId | undefined,
        selectedEluSource: SelectedEluSource
    ) => {
        this.setState((state) => ({
            ...state,
            selection: {
                ...state.selection,
                selectedEluId,
                selectedEluSource,
            },
        }));
        this.selectionsToAssociation();
    };

    private removeAssociation = (chair: ChairNumber) => {
        this.setState((state) => {
            if (!state.hemicycle) {
                throw Errors._3405a5ec();
            }
            const newAssociations = state.hemicycle.associations.filter(
                (a) => a.chairNumber !== chair
            );
            return {
                ...state,
                hemicycle: {
                    ...state.hemicycle,
                    associations: newAssociations,
                },
            };
        });
        this.updateAssociationsMaps();
    };

    private savePlan = (then: () => void) => {
        if (!this.state.hemicycle) {
            throw Errors._affb4796();
        }
        injector()
            .httpService.post(
                injector().urlBase + '/api/hemicyclePlans-associations',
                {
                    id: this.props.planId,
                    associations: this.state.hemicycle.associations,
                }
            )
            .then(() => {
                setTimeout(then, 500);
            });
    };

    private download = () => {};

    private archive = (then: () => void) => {};

    private protoEmpty = () => {
        this.setState((state) => ({
            ...state,
            associations: [],
        }));
        this.updateAssociationsMaps();
    };

    private switchHideAssociations = () =>
        this.setState((state) => {
            const hideAssociationsChairs = !state.config.hideAssociationsChairs;
            const selectedChairNumber = hideAssociationsChairs
                ? undefined
                : state.selection.selectedChairNumber;
            return {
                ...state,
                selection: {
                    ...state.selection,
                    selectedChairNumber,
                },
                config: {
                    ...state.config,
                    hideAssociationsChairs,
                },
            };
        });

    private switchDeleteMode = () =>
        this.setState((state) => {
            const deleteMode = !state.config.deleteMode;
            const selection = deleteMode
                ? {
                      selectedChairNumber: undefined,
                      selectedElu: undefined,
                      selectedEluSource: undefined,
                  }
                : state.selection;
            return {
                ...state,
                selection,
                config: {
                    ...state.config,
                    hideAssociationsChairs: false,
                    deleteMode,
                },
            };
        });

    private renderDebugButtons = () => (
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
            <button onClick={this.download}>Imprimer</button>
            <br />
            <button onClick={this.protoEmpty}>Vider</button>
            <button
                onClick={() => {}}
                css={css`
                    background: ${colors.redBackground};
                `}
            >
                Sample data
            </button>
        </div>
    );

    public render() {
        return (
            <SizingContainer
                render={(width: number, height: number) => {
                    const hemicycleWidth = (5 * width) / 6;
                    const columnWidth = width - hemicycleWidth;
                    return (
                        <div
                            css={css`
                                display: flex;
                                width: 100%;
                                height: ${height}px;
                                overflow: hidden;
                            `}
                        >
                            {options.debug && this.renderDebugButtons()}

                            {(!this.state.rawElus || !this.state.hemicycle) && (
                                <div
                                    css={css`
                                        width: 100%;
                                        height: 100%;
                                        display: flex;
                                        justify-content: center;
                                        align-items: center;
                                    `}
                                >
                                    <div>
                                        <LoadingIcon
                                            height={100}
                                            color={colors.grey}
                                        />
                                        <div
                                            css={css`
                                                font-size: 20px;
                                                color: ${colors.grey};
                                                padding-top: 20px;
                                            `}
                                        >
                                            Chargement...
                                        </div>
                                    </div>
                                </div>
                            )}

                            {this.state.rawElus &&
                                this.state.hemicycle &&
                                this.state.maps.eluById &&
                                this.state.maps.groupePolitiqueById &&
                                this.state.maps.associationByChair &&
                                this.state.maps.elusByGroupeId &&
                                this.state.maps.associationByEluId && (
                                    <React.Fragment>
                                        <div
                                            css={css`
                                                position: relative;
                                                width: ${hemicycleWidth}px;
                                                height: ${height}px;
                                            `}
                                        >
                                            <div
                                                css={css`
                                                    width: 60%;
                                                    margin: auto;
                                                    height: ${inputComponentHeight}px;
                                                `}
                                            >
                                                <InputsComponent
                                                    groupePolitiqueById={
                                                        this.state.maps
                                                            .groupePolitiqueById
                                                    }
                                                    selection={
                                                        this.state.selection
                                                    }
                                                    elus={
                                                        this.state.rawElus.elus
                                                    }
                                                    eluById={
                                                        this.state.maps.eluById
                                                    }
                                                    associationByChair={
                                                        this.state.maps
                                                            .associationByChair
                                                    }
                                                    updateSelectedChairNumber={
                                                        this
                                                            .updateSelectedChairNumber
                                                    }
                                                    updateSelectedEluId={
                                                        this.updateSelectedEluId
                                                    }
                                                    hemicycleConfigurationRendu={
                                                        this.state.hemicycle
                                                            .configurationRendu
                                                    }
                                                    deleteMode={
                                                        this.state.config
                                                            .deleteMode
                                                    }
                                                    switchDeleteMode={
                                                        this.switchDeleteMode
                                                    }
                                                    hideAssociationsChairs={
                                                        this.state.config
                                                            .hideAssociationsChairs
                                                    }
                                                    switchHideAssociations={
                                                        this
                                                            .switchHideAssociations
                                                    }
                                                    savePlan={this.savePlan}
                                                    archive={
                                                        !this.props.isProjet
                                                            ? this.archive
                                                            : undefined
                                                    }
                                                />
                                            </div>
                                            <Hemicycle
                                                eluById={
                                                    this.state.maps.eluById
                                                }
                                                groupePolitiques={
                                                    this.state.rawElus
                                                        .groupePolitiques
                                                }
                                                groupePolitiqueById={
                                                    this.state.maps
                                                        .groupePolitiqueById
                                                }
                                                associationByChair={
                                                    this.state.maps
                                                        .associationByChair
                                                }
                                                width={hemicycleWidth}
                                                height={
                                                    height -
                                                    inputComponentHeight
                                                }
                                                configurationRendu={
                                                    this.state.hemicycle
                                                        .configurationRendu
                                                }
                                                selectedChairNumber={
                                                    this.state.selection
                                                        .selectedChairNumber
                                                }
                                                updateSelectedChairNumber={
                                                    this
                                                        .updateSelectedChairNumber
                                                }
                                                hideAssociationsChairs={
                                                    this.state.config
                                                        .hideAssociationsChairs
                                                }
                                                removeAssociation={
                                                    this.removeAssociation
                                                }
                                                deleteMode={
                                                    this.state.config.deleteMode
                                                }
                                                printMode={false}
                                            />
                                        </div>
                                        <div
                                            css={css`
                                                width: ${columnWidth}px;
                                                height: ${height}px;
                                            `}
                                        >
                                            <EluListComponent
                                                elus={this.state.rawElus.elus}
                                                groupePolitiques={
                                                    this.state.rawElus
                                                        .groupePolitiques
                                                }
                                                associationByEluId={
                                                    this.state.maps
                                                        .associationByEluId
                                                }
                                                elusByGroupeId={
                                                    this.state.maps
                                                        .elusByGroupeId
                                                }
                                                eluById={
                                                    this.state.maps.eluById
                                                }
                                                associations={
                                                    this.state.hemicycle
                                                        .associations
                                                }
                                                selectedEluId={
                                                    this.state.selection
                                                        .selectedEluId
                                                }
                                                updateSelectedEluId={
                                                    this.updateSelectedEluId
                                                }
                                                removeAssociation={
                                                    this.removeAssociation
                                                }
                                                deleteMode={
                                                    this.state.config.deleteMode
                                                }
                                            />
                                        </div>
                                    </React.Fragment>
                                )}
                        </div>
                    );
                }}
            />
        );
    }
}
