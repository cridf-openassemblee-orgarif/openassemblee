/** @jsx jsx */
import { css, jsx } from '@emotion/core';
import * as React from 'react';
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
    PlanId,
} from '../domain/nominal';
import { Dict } from '../utils';
import { Errors } from './util/errors';
import { Association, HemicyclePlanAssociations } from '../domain/hemicycle';
import LoadingIcon from './util/LoadingIcon';
import { HemicyclePlanUpdateDTO } from '../domain/ws';
import { RawElus } from './util/DataService';

export type SelectedEluSource = 'input' | 'list';

interface Props {
    planId: PlanId;
    isProjet: boolean;
}

export interface MapData {
    eluById: Dict<EluId, Elu>;
    elusByGroupeId: Dict<GroupePolitiqueId, Elu[]>;
    groupePolitiqueById: Dict<GroupePolitiqueId, GroupePolitique>;
    elusDemissionaires: Elu[];
    elusSansGroupe: Elu[];
    associationByChair: Dict<ChairNumber, Association>;
    associationByEluId: Dict<EluId, Association>;
}

interface State {
    rawElus?: RawElus;
    hemicycle?: HemicyclePlanAssociations;
    data?: MapData;
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
        data: undefined,
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
            .dataService.fetchData(this.props.planId)
            .then((r) => {
                const rawElus = r.rawElus;
                const hemicycle = r.hemicycle;
                const {
                    eluById,
                    elusByGroupeId,
                    groupePolitiqueById,
                    elusDemissionaires,
                    elusSansGroupe,
                } = injector().dataService.rawElusMaps(r.rawElus);
                const {
                    associationByChair,
                    associationByEluId,
                } = injector().dataService.associationMaps(
                    hemicycle.associations
                );
                this.setState((state) => ({
                    ...state,
                    rawElus,
                    hemicycle,
                    data: {
                        eluById,
                        elusByGroupeId,
                        groupePolitiqueById,
                        elusDemissionaires,
                        elusSansGroupe,
                        associationByChair,
                        associationByEluId,
                    },
                }));
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

    private updateAssociationsMaps = () => {
        if (!this.state.hemicycle) {
            throw Errors._c72c4a64();
        }
        const {
            associationByChair,
            associationByEluId,
        } = injector().dataService.associationMaps(
            this.state.hemicycle.associations
        );
        this.setState((state) => {
            if (!state.data) {
                throw Errors._f37f755a();
            }
            return {
                ...state,
                data: {
                    ...state.data,
                    associationByChair,
                    associationByEluId,
                },
            };
        });
    };

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
        if (!this.state.hemicycle || !this.state.rawElus || !this.state.data) {
            throw Errors._affb4796();
        }
        injector().archiveService.saveArchive(
            this.props.planId,
            this.state.rawElus,
            this.state.hemicycle,
            this.state.data,
            then
        );
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
                                this.state.data && (
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
