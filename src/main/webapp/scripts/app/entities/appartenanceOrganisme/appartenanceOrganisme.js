"use strict";

angular.module("openassembleeApp").config(function ($stateProvider) {
    $stateProvider
        .state("appartenanceOrganisme", {
            parent: "entity",
            url: "/appartenanceOrganismes",
            data: {
                authorities: ["ROLE_USER"],
                pageTitle: "AppartenanceOrganismes",
            },
            views: {
                "content@": {
                    templateUrl:
                        "scripts/app/entities/appartenanceOrganisme/appartenanceOrganismes.html",
                    controller: "AppartenanceOrganismeController",
                },
            },
            resolve: {},
        })
        .state("appartenanceOrganisme.detail", {
            parent: "entity",
            url: "/appartenanceOrganisme/{id}",
            data: {
                authorities: ["ROLE_USER"],
                pageTitle: "AppartenanceOrganisme",
            },
            views: {
                "content@": {
                    templateUrl:
                        "scripts/app/entities/appartenanceOrganisme/appartenanceOrganisme-detail.html",
                    controller: "AppartenanceOrganismeDetailController",
                },
            },
            resolve: {
                entity: [
                    "$stateParams",
                    "AppartenanceOrganisme",
                    function ($stateParams, AppartenanceOrganisme) {
                        return AppartenanceOrganisme.get({
                            id: $stateParams.id,
                        });
                    },
                ],
            },
        })
        .state("appartenanceOrganisme.new", {
            parent: "appartenanceOrganisme",
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
                                "scripts/app/entities/appartenanceOrganisme/appartenanceOrganisme-dialog.html",
                            controller: "AppartenanceOrganismeDialogController",
                            size: "lg",
                            resolve: {
                                entity: function () {
                                    return {
                                        statut: null,
                                        organisme: null,
                                        codeRNE: null,
                                        dateDebut: null,
                                        dateFin: null,
                                        motifFin: null,
                                        dateNomination: null,
                                        reference: null,
                                        type: null,
                                        lienPiece: null,
                                        id: null,
                                    };
                                },
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("appartenanceOrganisme", null, {
                                    reload: true,
                                });
                            },
                            function () {
                                $state.go("appartenanceOrganisme");
                            }
                        );
                },
            ],
        })
        .state("appartenanceOrganisme.edit", {
            parent: "appartenanceOrganisme",
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
                                "scripts/app/entities/appartenanceOrganisme/appartenanceOrganisme-dialog.html",
                            controller: "AppartenanceOrganismeDialogController",
                            size: "lg",
                            resolve: {
                                entity: [
                                    "AppartenanceOrganisme",
                                    function (AppartenanceOrganisme) {
                                        return AppartenanceOrganisme.get({
                                            id: $stateParams.id,
                                        });
                                    },
                                ],
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("appartenanceOrganisme", null, {
                                    reload: true,
                                });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("appartenanceOrganisme.delete", {
            parent: "appartenanceOrganisme",
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
                                "scripts/app/entities/appartenanceOrganisme/appartenanceOrganisme-delete-dialog.html",
                            controller: "AppartenanceOrganismeDeleteController",
                            size: "md",
                            resolve: {
                                entity: [
                                    "AppartenanceOrganisme",
                                    function (AppartenanceOrganisme) {
                                        return AppartenanceOrganisme.get({
                                            id: $stateParams.id,
                                        });
                                    },
                                ],
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("appartenanceOrganisme", null, {
                                    reload: true,
                                });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        });
});
