"use strict";

angular.module("openassembleeApp").config(function ($stateProvider) {
    $stateProvider
        .state("mandature", {
            parent: "entity",
            url: "/mandatures",
            data: {
                authorities: ["ROLE_USER"],
                pageTitle: "Mandatures",
            },
            views: {
                "content@": {
                    templateUrl:
                        "scripts/app/entities/mandature/mandatures.html",
                    controller: "MandatureController",
                },
            },
            resolve: {},
        })
        .state("mandature.detail", {
            parent: "entity",
            url: "/mandature/{id}",
            data: {
                authorities: ["ROLE_USER"],
                pageTitle: "Mandature",
            },
            views: {
                "content@": {
                    templateUrl:
                        "scripts/app/entities/mandature/mandature-detail.html",
                    controller: "MandatureDetailController",
                },
            },
            resolve: {
                entity: [
                    "$stateParams",
                    "Mandature",
                    function ($stateParams, Mandature) {
                        return Mandature.get({ id: $stateParams.id });
                    },
                ],
            },
        })
        .state("mandature.new", {
            parent: "mandature",
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
                                "scripts/app/entities/mandature/mandature-dialog.html",
                            controller: "MandatureDialogController",
                            size: "lg",
                            resolve: {
                                entity: function () {
                                    return {
                                        anneeDebut: null,
                                        anneeFin: null,
                                        dateDebut: null,
                                        current: null,
                                        id: null,
                                    };
                                },
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("mandature", null, { reload: true });
                            },
                            function () {
                                $state.go("mandature");
                            }
                        );
                },
            ],
        })
        .state("mandature.edit", {
            parent: "mandature",
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
                                "scripts/app/entities/mandature/mandature-dialog.html",
                            controller: "MandatureDialogController",
                            size: "lg",
                            resolve: {
                                entity: [
                                    "Mandature",
                                    function (Mandature) {
                                        return Mandature.get({
                                            id: $stateParams.id,
                                        });
                                    },
                                ],
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("mandature", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        })
        .state("mandature.delete", {
            parent: "mandature",
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
                                "scripts/app/entities/mandature/mandature-delete-dialog.html",
                            controller: "MandatureDeleteController",
                            size: "md",
                            resolve: {
                                entity: [
                                    "Mandature",
                                    function (Mandature) {
                                        return Mandature.get({
                                            id: $stateParams.id,
                                        });
                                    },
                                ],
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("mandature", null, { reload: true });
                            },
                            function () {
                                $state.go("^");
                            }
                        );
                },
            ],
        });
});
