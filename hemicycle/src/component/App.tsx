/** @jsx jsx */
import { css, jsx } from '@emotion/core';
import * as React from 'react';
import { ReactElement } from 'react';
import { injector } from '../service/injector';
import SizingContainer from './util/SizingContainer';
import Hemicycle from './Hemicycle';
import EluListComponent from './list/EluListComponent';
import InputsComponent, { inputComponentHeight } from './input/InputsComponent';
import { colors, options, urls } from '../constants';
import { Unsuscriber } from '../service/EventBus';
import { Elu, GroupePolitique } from '../domain/elu';
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
import {
    EluListDTO,
    GroupePolitiqueFromWs,
    HemicycleArchiveCreationDTO,
    HemicyclePlanUpdateDTO,
} from '../domain/ws';
import * as ReactDomServer from 'react-dom/server';

export const associationMaps = (associations: Association[]) => {
    const associationByChair: Dict<ChairNumber, Association> = {};
    const associationByEluId: Dict<EluId, Association> = {};
    associations.forEach((a) => {
        set(associationByChair, a.chairNumber, a);
        set(associationByEluId, a.eluId, a);
    });
    return { associationByChair, associationByEluId };
};

export const rawElusMaps = (rawElus: {
    elus: Elu[];
    groupePolitiques: GroupePolitique[];
}) => {
    const eluById: Dict<EluId, Elu> = {};
    const elusByGroupeId: Dict<GroupePolitiqueId, Elu[]> = {};
    const elusSansGroupe: Elu[] = [];
    const elusDemissionaires: Elu[] = [];
    const groupePolitiqueById: Dict<GroupePolitiqueId, GroupePolitique> = {};
    rawElus.elus.forEach((e) => {
        set(eluById, e.id, e);
    });
    rawElus.groupePolitiques.forEach((gp) => {
        set(elusByGroupeId, gp.id, []);
        set(groupePolitiqueById, gp.id, gp);
    });
    // FIXMENOW filtrer les demissionaires ? ou et comment
    rawElus.elus.forEach((e) => {
        if (!e.actif) {
            elusDemissionaires.push(e);
        } else if (!e.groupePolitiqueId) {
            elusSansGroupe.push(e);
        } else {
            get(elusByGroupeId, e.groupePolitiqueId).push(e);
        }
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
        eluById,
        elusByGroupeId,
        groupePolitiqueById,
        elusDemissionaires,
        elusSansGroupe,
    };
};

const convertElu = (dto: EluListDTO): Elu => ({
    id: dto.elu.id,
    civilite: dto.elu.civilite,
    nom: dto.elu.nom,
    prenom: dto.elu.prenom,
    groupePolitiqueId: dto.groupePolitique ? dto.groupePolitique.id : undefined,
    shortFonction: dto.shortFonction,
    actif: !dto.elu.dateDemission,
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
    data: {
        eluById?: Dict<EluId, Elu>;
        elusByGroupeId?: Dict<GroupePolitiqueId, Elu[]>;
        groupePolitiqueById?: Dict<GroupePolitiqueId, GroupePolitique>;
        associationByChair?: Dict<ChairNumber, Association>;
        associationByEluId?: Dict<EluId, Association>;
        elusDemissionaires?: Elu[];
        elusSansGroupe?: Elu[];
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
        data: {
            eluById: undefined,
            elusByGroupeId: undefined,
            groupePolitiqueById: undefined,
            associationByChair: undefined,
            associationByEluId: undefined,
            elusDemissionaires: undefined,
            elusSansGroupe: undefined,
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
            .httpService.get(injector().urlBase + urls.elus)
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
                    urls.hemicyclePlansAssociations +
                    '/' +
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
        return gps.sort(alphabeticSort((gp: GroupePolitique) => gp.nom));
    };

    private updateElusMaps = () =>
        this.setState((state) => {
            if (!state.rawElus) {
                throw Errors._b7d84f98();
            }
            const {
                eluById,
                elusByGroupeId,
                groupePolitiqueById,
                elusDemissionaires,
                elusSansGroupe,
            } = rawElusMaps(state.rawElus);
            return {
                ...state,
                data: {
                    ...state.data,
                    eluById,
                    elusByGroupeId,
                    groupePolitiqueById,
                    elusDemissionaires,
                    elusSansGroupe,
                },
            };
        });

    private updateAssociationsMaps = () =>
        this.setState((state) => {
            if (!state.hemicycle) {
                return state;
            }
            const { associationByChair, associationByEluId } = associationMaps(
                state.hemicycle.associations
            );
            return {
                ...state,
                data: {
                    ...state.data,
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
        const dto: HemicyclePlanUpdateDTO = {
            id: this.props.planId,
            associations: this.state.hemicycle.associations,
        };
        injector()
            .httpService.post(
                injector().urlBase + urls.hemicyclePlansAssociations,
                dto
            )
            .then(() => setTimeout(then, 500));
    };

    private archive = (then: () => void) => {
        if (
            !this.state.hemicycle ||
            !this.state.rawElus ||
            !this.state.data.eluById ||
            !this.state.data.groupePolitiqueById ||
            !this.state.data.associationByChair ||
            !this.state.data.elusDemissionaires ||
            !this.state.data.elusSansGroupe
        ) {
            throw Errors._affb4796();
        }
        const renderReact = (svgElement: ReactElement) => {
            const svg = ReactDomServer.renderToString(svgElement);
            // FIXMENOW [doc] da fuck Batik semble ne pas aimer ça.... parce que je parse mal ?
            // FIXMENOW rechecker en fait....
            return '<?xml version="1.0" encoding="UTF-8"?>' + svg;
            // return finalSvg;
        };
        const svg = renderReact(
            <Hemicycle
                width={1600}
                height={1000}
                eluById={this.state.data.eluById}
                groupePolitiques={this.state.rawElus.groupePolitiques}
                groupePolitiqueById={this.state.data.groupePolitiqueById}
                associationByChair={this.state.data.associationByChair}
                configurationRendu={this.state.hemicycle.configurationRendu}
                selectedChairNumber={undefined}
                updateSelectedChairNumber={this.updateSelectedChairNumber}
                hideAssociationsChairs={false}
                removeAssociation={this.removeAssociation}
                deleteMode={false}
                printMode={true}
            />
        );
        const dto: HemicycleArchiveCreationDTO = {
            planId: this.props.planId,
            data: {
                associations: this.state.hemicycle.associations,
                ...this.state.rawElus,
            },
            svgPlan: svg,
        };
        injector()
            .httpService.post(injector().urlBase + urls.hemicycleArchives, dto)
            .then(() => setTimeout(then, 500));
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
            <button onClick={() => {}}>Vider</button>
            <button onClick={() => {}}>Sample data</button>
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
                                this.state.data.eluById &&
                                this.state.data.groupePolitiqueById &&
                                this.state.data.associationByChair &&
                                this.state.data.elusByGroupeId &&
                                this.state.data.associationByEluId &&
                                this.state.data.elusDemissionaires &&
                                this.state.data.elusSansGroupe && (
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
                                                        this.state.data
                                                            .groupePolitiqueById
                                                    }
                                                    selection={
                                                        this.state.selection
                                                    }
                                                    elus={
                                                        this.state.rawElus.elus
                                                    }
                                                    eluById={
                                                        this.state.data.eluById
                                                    }
                                                    associationByChair={
                                                        this.state.data
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
                                                    this.state.data.eluById
                                                }
                                                groupePolitiques={
                                                    this.state.rawElus
                                                        .groupePolitiques
                                                }
                                                groupePolitiqueById={
                                                    this.state.data
                                                        .groupePolitiqueById
                                                }
                                                associationByChair={
                                                    this.state.data
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
                                                    this.state.data
                                                        .associationByEluId
                                                }
                                                elusByGroupeId={
                                                    this.state.data
                                                        .elusByGroupeId
                                                }
                                                elusDemissionaires={
                                                    this.state.data
                                                        .elusDemissionaires
                                                }
                                                elusSansGroupe={
                                                    this.state.data
                                                        .elusSansGroupe
                                                }
                                                eluById={
                                                    this.state.data.eluById
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
