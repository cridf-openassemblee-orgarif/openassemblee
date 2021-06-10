"use strict";

angular.module("openassembleeApp").config(function ($stateProvider) {
    $stateProvider
        .state("appartenanceCommissionThematique", {
            parent: "entity",
            url: "/appartenanceCommissionThematiques",
            data: {
                authorities: ["ROLE_USER"],
                pageTitle: "AppartenanceCommissionThematiques",
            },
            views: {
                "content@": {
                    templateUrl:
                        "scripts/app/entities/appartenanceCommissionThematique/appartenanceCommissionThematiques.html",
                    controller: "AppartenanceCommissionThematiqueController",
                },
            },
            resolve: {},
        })
        .state("appartenanceCommissionThematique.detail", {
            parent: "entity",
            url: "/appartenanceCommissionThematique/{id}",
            data: {
                authorities: ["ROLE_USER"],
                pageTitle: "AppartenanceCommissionThematique",
            },
            views: {
                "content@": {
                    templateUrl:
                        "scripts/app/entities/appartenanceCommissionThematique/appartenanceCommissionThematique-detail.html",
                    controller:
                        "AppartenanceCommissionThematiqueDetailController",
                },
            },
            resolve: {
                entity: [
                    "$stateParams",
                    "AppartenanceCommissionThematique",
                    function ($stateParams, AppartenanceCommissionThematique) {
                        return AppartenanceCommissionThematique.get({
                            id: $stateParams.id,
                        });
                    },
                ],
            },
        })
        .state("appartenanceCommissionThematique.new", {
            parent: "appartenanceCommissionThematique",
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
                                "scripts/app/entities/appartenanceCommissionThematique/appartenanceCommissionThematique-dialog.html",
                            controller:
                                "AppartenanceCommissionThematiqueDialogController",
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
                                $state.go(
                                    "appartenanceCommissionThematique",
                                    null,
                                    { reload: true }
                                );
                            },
                            function () {
                                $state.go("appartenanceCommissionThematique");
                            }
                        );
                },
            ],
        })
        .state("appartenanceCommissionThematique.edit", {
            parent: "appartenanceCommissionThematique",
            url: "/{id}/edit",
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
                                            { id: $stateParams.id }
                                        );
                                    },
                                ],
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go(
                                    "appartenanceCommissionThematique",
                                    null,
                                    { reload: true }
                                );
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("appartenanceCommissionThematique.delete", {
            parent: "appartenanceCommissionThematique",
            url: "/{id}/delete",
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
                                    "AppartenanceCommissionThematique",
                                    function (
                                        AppartenanceCommissionThematique
                                    ) {
                                        return AppartenanceCommissionThematique.get(
                                            { id: $stateParams.id }
                                        );
                                    },
                                ],
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go(
                                    "appartenanceCommissionThematique",
                                    null,
                                    { reload: true }
                                );
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        });
});
