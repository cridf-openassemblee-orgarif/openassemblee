"use strict";

angular.module("openassembleeApp").config(function ($stateProvider) {
    $stateProvider
        .state("elu", {
            parent: "entity",
            url: "/elus",
            data: {
                // TODO mlo keep ça en fait ?
                pageTitle: "Élus",
            },
            views: {
                "content@": {
                    templateUrl: "scripts/app/entities/elu/elus.html",
                    controller: "EluController",
                },
            },
            resolve: {
                entity: [
                    "Elu",
                    function (Elu) {
                        return Elu.query();
                    },
                ],
            },
        })
        .state("elu.new", {
            parent: "elu",
            url: "/new",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/elu/elu-dialog.html",
                            controller: "EluDialogController",
                            size: "lg",
                            resolve: {
                                entity: function () {
                                    return {
                                        elu: {
                                            civilite: "MADAME",
                                            nom: null,
                                            prenom: null,
                                            nomJeuneFille: null,
                                            profession: null,
                                            dateNaissance: null,
                                            lieuNaissance: null,
                                            id: null,
                                        },
                                    };
                                },
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go(
                                    "elu.detail",
                                    { id: result.id },
                                    { reload: true }
                                );
                            },
                            function () {
                                $state.go("elu");
                            }
                        );
                },
            ],
        })
        .state("elu.detail", {
            parent: "elu",
            // FIXME nommer le paramètre ici eluId eut été malin au départ...
            url: "/{id}",
            data: {
                pageTitle: "Élu",
            },
            views: {
                "content@": {
                    templateUrl: "scripts/app/entities/elu/elu-detail.html",
                    controller: "EluDetailController",
                },
            },
            resolve: {
                entity: [
                    "$stateParams",
                    "Elu",
                    function ($stateParams, Elu) {
                        return Elu.get({ id: $stateParams.id });
                    },
                ],
            },
        })
        .state("elu.edit", {
            parent: "elu.detail",
            url: "/edit",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/elu/elu-dialog.html",
                            controller: "EluDialogController",
                            size: "lg",
                            resolve: {
                                entity: [
                                    "Elu",
                                    function (Elu) {
                                        return Elu.get({ id: $stateParams.id });
                                    },
                                ],
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.nouvelleFonctionExecutive", {
            parent: "elu.detail",
            url: "/nouvelle-fonction-executive",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/fonctionExecutive/fonctionExecutive-dialog.html",
                            controller: "FonctionExecutiveDialogController",
                            size: "lg",
                            resolve: {
                                entity: function () {
                                    return {
                                        fonction: null,
                                        dateDebut: null,
                                        dateFin: null,
                                        motifFin: null,
                                        id: null,
                                    };
                                },
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.editFonctionExecutive", {
            parent: "elu.detail",
            url: "/edit-fonction-executive/{fonctionId}",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/fonctionExecutive/fonctionExecutive-dialog.html",
                            controller: "FonctionExecutiveDialogController",
                            size: "lg",
                            resolve: {
                                entity: [
                                    "FonctionExecutive",
                                    function (FonctionExecutive) {
                                        return FonctionExecutive.get({
                                            id: $stateParams.fonctionId,
                                        });
                                    },
                                ],
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.supprimerFonctionExecutive", {
            parent: "elu.detail",
            url: "/fonction-executive/{fonctionId}/delete",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/fonctionExecutive/fonctionExecutive-delete-dialog.html",
                            controller: "FonctionExecutiveDeleteController",
                            size: "md",
                            resolve: {
                                entity: [
                                    function () {
                                        return {
                                            id: $stateParams.fonctionId,
                                        };
                                    },
                                ],
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.ajoutDistinctionHonorifique", {
            parent: "elu.detail",
            url: "/distinction-honorifique",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/distinctionHonorifique/distinctionHonorifique-dialog.html",
                            controller:
                                "DistinctionHonorifiqueDialogController",
                            size: "lg",
                            resolve: {
                                entity: function () {
                                    return {
                                        titre: null,
                                        date: null,
                                        id: null,
                                    };
                                },
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.editionDistinctionHonorifique", {
            parent: "elu.detail",
            url: "/distinction-honorifique/{distinctionHonorifiqueId}",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/distinctionHonorifique/distinctionHonorifique-dialog.html",
                            controller:
                                "DistinctionHonorifiqueDialogController",
                            size: "lg",
                            resolve: {
                                entity: [
                                    "DistinctionHonorifique",
                                    function (DistinctionHonorifique) {
                                        return DistinctionHonorifique.get({
                                            id: $stateParams.distinctionHonorifiqueId,
                                        });
                                    },
                                ],
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.supprimerDistinctionHonorifique", {
            parent: "elu.detail",
            url: "/distinction-honorifique/{distinctionHonorifiqueId}/delete",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/distinctionHonorifique/distinctionHonorifique-delete-dialog.html",
                            controller:
                                "DistinctionHonorifiqueDeleteController",
                            size: "md",
                            resolve: {
                                entity: [
                                    function () {
                                        return {
                                            id: $stateParams.distinctionHonorifiqueId,
                                        };
                                    },
                                ],
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.nouvelleFonctionCommissionPermanente", {
            parent: "elu.detail",
            url: "/nouvelle-fonction-commission-permanente",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/fonctionCommissionPermanente/fonctionCommissionPermanente-dialog.html",
                            controller:
                                "FonctionCommissionPermanenteDialogController",
                            size: "lg",
                            resolve: {
                                entity: function () {
                                    return {
                                        fonction: null,
                                        dateDebut: null,
                                        dateFin: null,
                                        motifFin: null,
                                        id: null,
                                    };
                                },
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.editFonctionCommissionPermanente", {
            parent: "elu.detail",
            url: "/edit-fonction-commission-permanente/{fonctionId}",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/fonctionCommissionPermanente/fonctionCommissionPermanente-dialog.html",
                            controller:
                                "FonctionCommissionPermanenteDialogController",
                            size: "lg",
                            resolve: {
                                entity: [
                                    "FonctionCommissionPermanente",
                                    function (FonctionCommissionPermanente) {
                                        return FonctionCommissionPermanente.get(
                                            { id: $stateParams.fonctionId }
                                        );
                                    },
                                ],
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.supprimerFonctionCommissionPermanente", {
            parent: "elu.detail",
            url: "/fonction-commission-permanente/{fonctionId}/delete",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/fonctionCommissionPermanente/fonctionCommissionPermanente-delete-dialog.html",
                            controller:
                                "FonctionCommissionPermanenteDeleteController",
                            size: "md",
                            resolve: {
                                entity: [
                                    function () {
                                        return {
                                            id: $stateParams.fonctionId,
                                        };
                                    },
                                ],
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.ajouterCommissionPermanente", {
            parent: "elu.detail",
            url: "/ajouter-commission-permanente",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/appartenanceCommissionPermanente/appartenanceCommissionPermanente-dialog.html",
                            controller:
                                "AppartenanceCommissionPermanenteDialogController",
                            size: "lg",
                            resolve: {
                                entity: function () {
                                    return {
                                        dateDebut: null,
                                        dateFin: null,
                                        motifFin: null,
                                        id: null,
                                    };
                                },
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.editAppartenanceCommissionPermanente", {
            parent: "elu.detail",
            url: "/edit-appartenance-commission-permanente/{appartenanceId}",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/appartenanceCommissionPermanente/appartenanceCommissionPermanente-dialog.html",
                            controller:
                                "AppartenanceCommissionPermanenteDialogController",
                            size: "lg",
                            resolve: {
                                entity: [
                                    "AppartenanceCommissionPermanente",
                                    function (
                                        AppartenanceCommissionPermanente
                                    ) {
                                        return AppartenanceCommissionPermanente.get(
                                            { id: $stateParams.appartenanceId }
                                        );
                                    },
                                ],
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.supprimerAppartenanceCommissionPermanente", {
            parent: "elu.detail",
            url: "/appartenance-commission-permanente/{fonctionId}/delete",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/appartenanceCommissionPermanente/appartenanceCommissionPermanente-delete-dialog.html",
                            controller:
                                "AppartenanceCommissionPermanenteDeleteController",
                            size: "md",
                            resolve: {
                                entity: [
                                    function () {
                                        return {
                                            id: $stateParams.fonctionId,
                                        };
                                    },
                                ],
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.ajouterGroupePolitique", {
            parent: "elu.detail",
            url: "/ajouter-groupe-politique",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/appartenanceGroupePolitique/appartenanceGroupePolitique-dialog.html",
                            controller:
                                "AppartenanceGroupePolitiqueDialogController",
                            size: "lg",
                            resolve: {
                                entity: function () {
                                    return {
                                        dateDebut: null,
                                        dateFin: null,
                                        motifFin: null,
                                        id: null,
                                    };
                                },
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.editAppartenanceGroupePolitique", {
            parent: "elu.detail",
            url: "/edit-appartenance-groupe-politique/{appartenanceId}",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/appartenanceGroupePolitique/appartenanceGroupePolitique-dialog.html",
                            controller:
                                "AppartenanceGroupePolitiqueDialogController",
                            size: "lg",
                            resolve: {
                                entity: [
                                    "AppartenanceGroupePolitique",
                                    function (AppartenanceGroupePolitique) {
                                        return AppartenanceGroupePolitique.get({
                                            id: $stateParams.appartenanceId,
                                        });
                                    },
                                ],
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.supprimerAppartenanceGroupePolitique", {
            parent: "elu.detail",
            url: "/appartenance-groupe-politique/{appartenanceId}/delete",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/appartenanceGroupePolitique/appartenanceGroupePolitique-delete-dialog.html",
                            controller:
                                "AppartenanceGroupePolitiqueDeleteController",
                            size: "md",
                            resolve: {
                                entity: [
                                    function () {
                                        return {
                                            id: $stateParams.appartenanceId,
                                        };
                                    },
                                ],
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.autreMandat", {
            parent: "elu.detail",
            url: "/autre-mandat",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/autreMandat/autreMandat-dialog.html",
                            controller: "AutreMandatDialogController",
                            size: "lg",
                            resolve: {
                                title: function () {
                                    return "Ajouter un autre mandat";
                                },
                                entity: function () {
                                    return {};
                                },
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.editAutreMandat", {
            parent: "elu.detail",
            url: "/autre-mandat/{mandatId}",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/autreMandat/autreMandat-dialog.html",
                            controller: "AutreMandatDialogController",
                            size: "lg",
                            resolve: {
                                title: function () {
                                    return "Modifier le mandat";
                                },
                                entity: function (AutreMandat) {
                                    return AutreMandat.get({
                                        id: $stateParams.mandatId,
                                    });
                                },
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.supprimerAutreMandat", {
            parent: "elu.detail",
            url: "/autre-mandat/{mandatId}/delete",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/autreMandat/autreMandat-delete-dialog.html",
                            controller: "AutreMandatDeleteController",
                            size: "md",
                            resolve: {
                                entity: [
                                    function () {
                                        return {
                                            id: $stateParams.mandatId,
                                        };
                                    },
                                ],
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.ajouterOrganisme", {
            parent: "elu.detail",
            url: "/ajouter-organisme",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/appartenanceOrganisme/appartenanceOrganisme-dialog.html",
                            controller: "AppartenanceOrganismeDialogController",
                            size: "lg",
                            resolve: {
                                entity: function () {
                                    return {};
                                },
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.editAppartenanceOrganisme", {
            parent: "elu.detail",
            url: "/edit-appartenance-organisme/{appartenanceId}",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/appartenanceOrganisme/appartenanceOrganisme-dialog.html",
                            controller: "AppartenanceOrganismeDialogController",
                            size: "lg",
                            resolve: {
                                entity: [
                                    "AppartenanceOrganisme",
                                    function (AppartenanceOrganisme) {
                                        return AppartenanceOrganisme.get({
                                            id: $stateParams.appartenanceId,
                                        });
                                    },
                                ],
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.nouvelleFonctionGroupePolitique", {
            parent: "elu.detail",
            url: "/nouvelle-fonction-groupe-politique/{groupePolitiqueId}",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/fonctionGroupePolitique/fonctionGroupePolitique-dialog.html",
                            controller:
                                "FonctionGroupePolitiqueDialogController",
                            size: "lg",
                            resolve: {
                                entity: function () {
                                    return {
                                        groupePolitique: {
                                            id: $stateParams.groupePolitiqueId,
                                        },
                                        fonction: null,
                                        dateDebut: null,
                                        dateFin: null,
                                        motifFin: null,
                                        id: null,
                                    };
                                },
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.editFonctionGroupePolitique", {
            parent: "elu.detail",
            url: "/edit-fonction-groupe-politique/{fonctionId}",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/fonctionGroupePolitique/fonctionGroupePolitique-dialog.html",
                            controller:
                                "FonctionGroupePolitiqueDialogController",
                            size: "lg",
                            resolve: {
                                entity: [
                                    "FonctionGroupePolitique",
                                    function (FonctionGroupePolitique) {
                                        return FonctionGroupePolitique.get({
                                            id: $stateParams.fonctionId,
                                        });
                                    },
                                ],
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.supprimerFonctionGroupePolitique", {
            parent: "elu.detail",
            url: "/fonction-groupe-politique/{fonctionId}/delete",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/fonctionGroupePolitique/fonctionGroupePolitique-delete-dialog.html",
                            controller:
                                "FonctionGroupePolitiqueDeleteController",
                            size: "md",
                            resolve: {
                                entity: [
                                    function () {
                                        return {
                                            id: $stateParams.fonctionId,
                                        };
                                    },
                                ],
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.ajouterCommissionThematique", {
            parent: "elu.detail",
            url: "/ajouter-commission-thematique",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/appartenanceCommissionThematique/appartenanceCommissionThematique-dialog.html",
                            controller:
                                "AppartenanceCommissionThematiqueDialogController",
                            size: "lg",
                            resolve: {
                                entity: function () {
                                    return {};
                                },
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.editAppartenanceCommissionThematique", {
            parent: "elu.detail",
            url: "/edit-appartenance-commission-thematique/{appartenanceId}",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/appartenanceCommissionThematique/appartenanceCommissionThematique-dialog.html",
                            controller:
                                "AppartenanceCommissionThematiqueDialogController",
                            size: "lg",
                            resolve: {
                                entity: [
                                    "AppartenanceCommissionThematique",
                                    function (
                                        AppartenanceCommissionThematique
                                    ) {
                                        return AppartenanceCommissionThematique.get(
                                            { id: $stateParams.appartenanceId }
                                        );
                                    },
                                ],
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.supprimerAppartenanceCommissionThematique", {
            parent: "elu.detail",
            url: "/appartenance-commission-thematique/{appartenanceId}/delete",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/appartenanceCommissionThematique/appartenanceCommissionThematique-delete-dialog.html",
                            controller:
                                "AppartenanceCommissionThematiqueDeleteController",
                            size: "md",
                            resolve: {
                                entity: [
                                    function () {
                                        return {
                                            id: $stateParams.appartenanceId,
                                        };
                                    },
                                ],
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.nouvelleFonctionCommissionThematique", {
            parent: "elu.detail",
            url: "/nouvelle-fonction-commission-thematique",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/fonctionCommissionThematique/fonctionCommissionThematique-dialog.html",
                            controller:
                                "FonctionCommissionThematiqueDialogController",
                            size: "lg",
                            resolve: {
                                entity: function () {
                                    return {};
                                },
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.editFonctionCommissionThematique", {
            parent: "elu.detail",
            url: "/edit-fonction-commission-thematique/{fonctionId}",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/fonctionCommissionThematique/fonctionCommissionThematique-dialog.html",
                            controller:
                                "FonctionCommissionThematiqueDialogController",
                            size: "lg",
                            resolve: {
                                entity: [
                                    "FonctionCommissionThematique",
                                    function (FonctionCommissionThematique) {
                                        return FonctionCommissionThematique.get(
                                            { id: $stateParams.fonctionId }
                                        );
                                    },
                                ],
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.supprimerFonctionCommissionThematique", {
            parent: "elu.detail",
            url: "/fonction-commission-thematique/{fonctionId}/delete",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/fonctionCommissionThematique/fonctionCommissionThematique-delete-dialog.html",
                            controller:
                                "FonctionCommissionThematiqueDeleteController",
                            size: "md",
                            resolve: {
                                entity: [
                                    function () {
                                        return {
                                            id: $stateParams.fonctionId,
                                        };
                                    },
                                ],
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.uploadImage", {
            parent: "elu.detail",
            url: "/upload-image",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/upload/upload-image-dialog.html",
                            controller: "UploadImageEluDialogController",
                            size: "lg",
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.ajouterAdressePostale", {
            parent: "elu.detail",
            url: "/adressePostale",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/adressePostale/adressePostale-dialog.html",
                            controller: "AdressePostaleDialogController",
                            size: "lg",
                            resolve: {
                                entity: function () {
                                    return {
                                        natureProPerso: "PRO",
                                        voie: "",
                                        codePostal: "",
                                        ville: "",
                                        niveauConfidentialite: "INTERNE",
                                        adresseDeCorrespondance: false,
                                        publicationAnnuaire: false,
                                    };
                                },
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.editerAdressePostale", {
            parent: "elu.detail",
            url: "/adressePostale/{adressePostaleId}",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/adressePostale/adressePostale-dialog.html",
                            controller: "AdressePostaleDialogController",
                            size: "lg",
                            resolve: {
                                entity: [
                                    "AdressePostale",
                                    function (AdressePostale) {
                                        return AdressePostale.get({
                                            id: $stateParams.adressePostaleId,
                                        });
                                    },
                                ],
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.supprimerAdressePostale", {
            parent: "elu.detail",
            url: "/adressePostale/{adressePostaleId}/delete",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/adressePostale/adressePostale-delete-dialog.html",
                            controller: "AdressePostaleDeleteController",
                            size: "md",
                            resolve: {
                                entity: [
                                    function () {
                                        return {
                                            eluId: $stateParams.id,
                                            adressePostaleId:
                                                $stateParams.adressePostaleId,
                                        };
                                    },
                                ],
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.ajouterAdresseMail", {
            parent: "elu.detail",
            url: "/adresseMail",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/adresseMail/adresseMail-dialog.html",
                            controller: "AdresseMailDialogController",
                            size: "lg",
                            resolve: {
                                entity: function () {
                                    return {
                                        natureProPerso: "PRO",
                                        mail: "",
                                        niveauConfidentialite: "INTERNE",
                                        adresseDeCorrespondance: false,
                                        publicationAnnuaire: false,
                                    };
                                },
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.editerAdresseMail", {
            parent: "elu.detail",
            url: "/adresseMail/{adresseMailId}",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/adresseMail/adresseMail-dialog.html",
                            controller: "AdresseMailDialogController",
                            size: "lg",
                            resolve: {
                                entity: [
                                    "AdresseMail",
                                    function (AdresseMail) {
                                        return AdresseMail.get({
                                            id: $stateParams.adresseMailId,
                                        });
                                    },
                                ],
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.supprimerAdresseMail", {
            parent: "elu.detail",
            url: "/adresseMail/{adresseMailId}/delete",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/adresseMail/adresseMail-delete-dialog.html",
                            controller: "AdresseMailDeleteController",
                            size: "md",
                            resolve: {
                                entity: [
                                    function () {
                                        return {
                                            eluId: $stateParams.id,
                                            adresseMailId:
                                                $stateParams.adresseMailId,
                                        };
                                    },
                                ],
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.ajouterNumeroFax", {
            parent: "elu.detail",
            url: "/numeroFax",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/numeroFax/numeroFax-dialog.html",
                            controller: "NumeroFaxDialogController",
                            size: "lg",
                            resolve: {
                                entity: function () {
                                    return {
                                        natureProPerso: "PRO",
                                        numero: "",
                                        niveauConfidentialite: "INTERNE",
                                        publicationAnnuaire: false,
                                    };
                                },
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.editerNumeroFax", {
            parent: "elu.detail",
            url: "/numeroFax/{numeroFaxId}",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/numeroFax/numeroFax-dialog.html",
                            controller: "NumeroFaxDialogController",
                            size: "lg",
                            resolve: {
                                entity: [
                                    "NumeroFax",
                                    function (NumeroFax) {
                                        return NumeroFax.get({
                                            id: $stateParams.numeroFaxId,
                                        });
                                    },
                                ],
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.supprimerNumeroFax", {
            parent: "elu.detail",
            url: "/numeroFax/{numeroFaxId}/delete",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/numeroFax/numeroFax-delete-dialog.html",
                            controller: "NumeroFaxDeleteController",
                            size: "md",
                            resolve: {
                                entity: [
                                    function () {
                                        return {
                                            eluId: $stateParams.id,
                                            numeroFaxId:
                                                $stateParams.numeroFaxId,
                                        };
                                    },
                                ],
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.ajouterNumeroTelephone", {
            parent: "elu.detail",
            url: "/numeroTelephone",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/numeroTelephone/numeroTelephone-dialog.html",
                            controller: "NumeroTelephoneDialogController",
                            size: "lg",
                            resolve: {
                                entity: function () {
                                    return {
                                        natureProPerso: "PRO",
                                        natureFixeMobile: "MOBILE",
                                        numero: "",
                                        niveauConfidentialite: "INTERNE",
                                        publicationAnnuaire: false,
                                    };
                                },
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.editerNumeroTelephone", {
            parent: "elu.detail",
            url: "/numeroTelephone/{numeroTelephoneId}",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/numeroTelephone/numeroTelephone-dialog.html",
                            controller: "NumeroTelephoneDialogController",
                            size: "lg",
                            resolve: {
                                entity: [
                                    "NumeroTelephone",
                                    function (NumeroTelephone) {
                                        return NumeroTelephone.get({
                                            id: $stateParams.numeroTelephoneId,
                                        });
                                    },
                                ],
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.supprimerNumeroTelephone", {
            parent: "elu.detail",
            url: "/numeroTelephone/{numeroTelephoneId}/delete",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/numeroTelephone/numeroTelephone-delete-dialog.html",
                            controller: "NumeroTelephoneDeleteController",
                            size: "md",
                            resolve: {
                                entity: [
                                    function () {
                                        return {
                                            eluId: $stateParams.id,
                                            numeroTelephoneId:
                                                $stateParams.numeroTelephoneId,
                                        };
                                    },
                                ],
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.ajouterIdentiteInternet", {
            parent: "elu.detail",
            url: "/identiteInternet",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/identiteInternet/identiteInternet-dialog.html",
                            controller: "IdentiteInternetDialogController",
                            size: "lg",
                            resolve: {
                                entity: function () {
                                    return {};
                                },
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.editerIdentiteInternet", {
            parent: "elu.detail",
            url: "/identiteInternet/{identiteInternetId}",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/identiteInternet/identiteInternet-dialog.html",
                            controller: "IdentiteInternetDialogController",
                            size: "lg",
                            resolve: {
                                entity: [
                                    "IdentiteInternet",
                                    function (IdentiteInternet) {
                                        return IdentiteInternet.get({
                                            id: $stateParams.identiteInternetId,
                                        });
                                    },
                                ],
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.supprimerIdentiteInternet", {
            parent: "elu.detail",
            url: "/identiteInternet/{identiteInternetId}/delete",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/identiteInternet/identiteInternet-delete-dialog.html",
                            controller: "IdentiteInternetDeleteController",
                            size: "md",
                            resolve: {
                                entity: [
                                    function () {
                                        return {
                                            eluId: $stateParams.id,
                                            identiteInternetId:
                                                $stateParams.identiteInternetId,
                                        };
                                    },
                                ],
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.ajouterMandat", {
            parent: "elu.detail",
            url: "/ajouter-mandat",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/mandat/mandat-dialog.html",
                            controller: "MandatDialogController",
                            size: "lg",
                            resolve: {
                                entity: function () {
                                    return {};
                                },
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.editMandat", {
            parent: "elu.detail",
            url: "/edit-mandat/{mandatId}",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/mandat/mandat-dialog.html",
                            controller: "MandatDialogController",
                            size: "lg",
                            resolve: {
                                entity: [
                                    "Mandat",
                                    function (Mandat) {
                                        return Mandat.get({
                                            id: $stateParams.mandatId,
                                        });
                                    },
                                ],
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("elu.detail.supprimerMandat", {
            parent: "elu.detail",
            url: "/mandat/{mandatId}/delete",
            data: {
                authorities: ["ROLE_USER"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/entities/mandat/mandat-delete-dialog.html",
                            controller: "MandatDeleteController",
                            size: "md",
                            resolve: {
                                entity: [
                                    function () {
                                        return {
                                            id: $stateParams.mandatId,
                                        };
                                    },
                                ],
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("^", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        });
});
