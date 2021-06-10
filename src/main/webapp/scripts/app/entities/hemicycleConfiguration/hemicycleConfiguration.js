"use strict";

angular.module("openassembleeApp").config(function ($stateProvider) {
    $stateProvider
        .state("hemicycleConfiguration", {
            parent: "entity",
            url: "/hemicycleConfigurations",
            data: {
                authorities: ["ROLE_USER"],
                pageTitle: "HemicycleConfigurations",
            },
            views: {
                "content@": {
                    templateUrl:
                        "scripts/app/entities/hemicycleConfiguration/hemicycleConfigurations.html",
                    controller: "HemicycleConfigurationController",
                },
            },
            resolve: {},
        })
        .state("hemicycleConfiguration.detail", {
            parent: "entity",
            url: "/hemicycleConfiguration/{id}",
            data: {
                authorities: ["ROLE_USER"],
                pageTitle: "HemicycleConfiguration",
            },
            views: {
                "content@": {
                    templateUrl:
                        "scripts/app/entities/hemicycleConfiguration/hemicycleConfiguration-detail.html",
                    controller: "HemicycleConfigurationDetailController",
                },
            },
            resolve: {
                entity: [
                    "$stateParams",
                    "HemicycleConfiguration",
                    function ($stateParams, HemicycleConfiguration) {
                        return HemicycleConfiguration.get({
                            id: $stateParams.id,
                        });
                    },
                ],
            },
        })
        .state("hemicycleConfiguration.new", {
            parent: "hemicycleConfiguration",
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
                                "scripts/app/entities/hemicycleConfiguration/hemicycleConfiguration-dialog.html",
                            controller:
                                "HemicycleConfigurationDialogController",
                            size: "lg",
                            resolve: {
                                entity: function () {
                                    return {
                                        label: null,
                                        jsonConfiguration: null,
                                        creationDate: null,
                                        lastModificationDate: null,
                                        frozen: null,
                                        frozenDate: null,
                                        id: null,
                                    };
                                },
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("hemicycleConfiguration", null, {
                                    reload: true,
                                });
                            },
                            function () {
                                $state.go("hemicycleConfiguration");
                            }
                        );
                },
            ],
        })
        .state("hemicycleConfiguration.edit", {
            parent: "hemicycleConfiguration",
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
                                "scripts/app/entities/hemicycleConfiguration/hemicycleConfiguration-dialog.html",
                            controller:
                                "HemicycleConfigurationDialogController",
                            size: "lg",
                            resolve: {
                                entity: [
                                    "HemicycleConfiguration",
                                    function (HemicycleConfiguration) {
                                        return HemicycleConfiguration.get({
                                            id: $stateParams.id,
                                        });
                                    },
                                ],
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("hemicycleConfiguration", null, {
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
        .state("hemicycleConfiguration.delete", {
            parent: "hemicycleConfiguration",
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
                                "scripts/app/entities/hemicycleConfiguration/hemicycleConfiguration-delete-dialog.html",
                            controller:
                                "HemicycleConfigurationDeleteController",
                            size: "md",
                            resolve: {
                                entity: [
                                    "HemicycleConfiguration",
                                    function (HemicycleConfiguration) {
                                        return HemicycleConfiguration.get({
                                            id: $stateParams.id,
                                        });
                                    },
                                ],
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("hemicycleConfiguration", null, {
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
