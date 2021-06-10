"use strict";

angular.module("openassembleeApp").config(function ($stateProvider) {
    $stateProvider
        .state("fonctionCommissionThematique", {
            parent: "entity",
            url: "/fonctionCommissionThematiques",
            data: {
                authorities: ["ROLE_USER"],
                pageTitle: "FonctionCommissionThematiques",
            },
            views: {
                "content@": {
                    templateUrl:
                        "scripts/app/entities/fonctionCommissionThematique/fonctionCommissionThematiques.html",
                    controller: "FonctionCommissionThematiqueController",
                },
            },
            resolve: {},
        })
        .state("fonctionCommissionThematique.detail", {
            parent: "entity",
            url: "/fonctionCommissionThematique/{id}",
            data: {
                authorities: ["ROLE_USER"],
                pageTitle: "FonctionCommissionThematique",
            },
            views: {
                "content@": {
                    templateUrl:
                        "scripts/app/entities/fonctionCommissionThematique/fonctionCommissionThematique-detail.html",
                    controller: "FonctionCommissionThematiqueDetailController",
                },
            },
            resolve: {
                entity: [
                    "$stateParams",
                    "FonctionCommissionThematique",
                    function ($stateParams, FonctionCommissionThematique) {
                        return FonctionCommissionThematique.get({
                            id: $stateParams.id,
                        });
                    },
                ],
            },
        })
        .state("fonctionCommissionThematique.new", {
            parent: "fonctionCommissionThematique",
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
                                "scripts/app/entities/fonctionCommissionThematique/fonctionCommissionThematique-dialog.html",
                            controller:
                                "FonctionCommissionThematiqueDialogController",
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
                                $state.go(
                                    "fonctionCommissionThematique",
                                    null,
                                    { reload: true }
                                );
                            },
                            function () {
                                $state.go("fonctionCommissionThematique");
                            }
                        );
                },
            ],
        })
        .state("fonctionCommissionThematique.edit", {
            parent: "fonctionCommissionThematique",
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
                                "scripts/app/entities/fonctionCommissionThematique/fonctionCommissionThematique-dialog.html",
                            controller:
                                "FonctionCommissionThematiqueDialogController",
                            size: "lg",
                            resolve: {
                                entity: [
                                    "FonctionCommissionThematique",
                                    function (FonctionCommissionThematique) {
                                        return FonctionCommissionThematique.get(
                                            { id: $stateParams.id }
                                        );
                                    },
                                ],
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go(
                                    "fonctionCommissionThematique",
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
        .state("fonctionCommissionThematique.delete", {
            parent: "fonctionCommissionThematique",
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
                                "scripts/app/entities/fonctionCommissionThematique/fonctionCommissionThematique-delete-dialog.html",
                            controller:
                                "FonctionCommissionThematiqueDeleteController",
                            size: "md",
                            resolve: {
                                entity: [
                                    "FonctionCommissionThematique",
                                    function (FonctionCommissionThematique) {
                                        return FonctionCommissionThematique.get(
                                            { id: $stateParams.id }
                                        );
                                    },
                                ],
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go(
                                    "fonctionCommissionThematique",
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
