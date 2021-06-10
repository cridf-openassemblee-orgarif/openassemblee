"use strict";

angular.module("openassembleeApp").config(function ($stateProvider) {
    $stateProvider
        .state("mandat", {
            parent: "entity",
            url: "/mandats",
            data: {
                authorities: ["ROLE_USER"],
                pageTitle: "Mandats",
            },
            views: {
                "content@": {
                    templateUrl: "scripts/app/entities/mandat/mandats.html",
                    controller: "MandatController",
                },
            },
            resolve: {},
        })
        .state("mandat.detail", {
            parent: "entity",
            url: "/mandat/{id}",
            data: {
                authorities: ["ROLE_USER"],
                pageTitle: "Mandat",
            },
            views: {
                "content@": {
                    templateUrl:
                        "scripts/app/entities/mandat/mandat-detail.html",
                    controller: "MandatDetailController",
                },
            },
            resolve: {
                entity: [
                    "$stateParams",
                    "Mandat",
                    function ($stateParams, Mandat) {
                        return Mandat.get({ id: $stateParams.id });
                    },
                ],
            },
        })
        .state("mandat.new", {
            parent: "mandat",
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
                                "scripts/app/entities/mandat/mandat-dialog.html",
                            controller: "MandatDialogController",
                            size: "lg",
                            resolve: {
                                entity: function () {
                                    return {
                                        dateDebut: null,
                                        codeDepartement: null,
                                        departement: null,
                                        dateDemission: null,
                                        motifDemission: null,
                                        id: null,
                                    };
                                },
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("mandat", null, { reload: true });
                            },
                            function () {
                                $state.go("mandat");
                            }
                        );
                },
            ],
        })
        .state("mandat.edit", {
            parent: "mandat",
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
                                "scripts/app/entities/mandat/mandat-dialog.html",
                            controller: "MandatDialogController",
                            size: "lg",
                            resolve: {
                                entity: [
                                    "Mandat",
                                    function (Mandat) {
                                        return Mandat.get({
                                            id: $stateParams.id,
                                        });
                                    },
                                ],
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("mandat", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("mandat.delete", {
            parent: "mandat",
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
                                "scripts/app/entities/mandat/mandat-delete-dialog.html",
                            controller: "MandatDeleteController",
                            size: "md",
                            resolve: {
                                entity: [
                                    "Mandat",
                                    function (Mandat) {
                                        return Mandat.get({
                                            id: $stateParams.id,
                                        });
                                    },
                                ],
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("mandat", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        });
});
