"use strict";

angular.module("openassembleeApp").config(function ($stateProvider) {
    $stateProvider
        .state("user-management", {
            parent: "admin",
            url: "/user-management",
            data: {
                authorities: ["ROLE_ADMIN"],
                pageTitle: "openassemblee",
            },
            views: {
                "content@": {
                    templateUrl:
                        "scripts/app/admin/user-management/user-management.html",
                    controller: "UserManagementController",
                },
            },
            resolve: {},
        })
        .state("user-management-detail", {
            parent: "admin",
            url: "/user-management/:login",
            data: {
                authorities: ["ROLE_ADMIN"],
                pageTitle: "openassemblee",
            },
            views: {
                "content@": {
                    templateUrl:
                        "scripts/app/admin/user-management/user-management-detail.html",
                    controller: "UserManagementDetailController",
                },
            },
            resolve: {},
        })
        .state("user-management.new", {
            parent: "user-management",
            url: "/new",
            data: {
                authorities: ["ROLE_ADMIN"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/admin/user-management/user-management-dialog.html",
                            controller: "UserManagementDialogController",
                            size: "lg",
                            resolve: {
                                entity: function () {
                                    return {
                                        id: null,
                                        login: null,
                                        firstName: null,
                                        lastName: null,
                                        email: null,
                                        activated: null,
                                        langKey: null,
                                        createdBy: null,
                                        createdDate: null,
                                        lastModifiedBy: null,
                                        lastModifiedDate: null,
                                        resetDate: null,
                                        resetKey: null,
                                        authorities: null,
                                    };
                                },
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("user-management", null, {
                                    reload: true,
                                });
                            },
                            function () {
                                $state.go("user-management");
                            }
                        );
                },
            ],
        })
        .state("user-management.edit", {
            parent: "user-management",
            url: "/{login}/edit",
            data: {
                authorities: ["ROLE_ADMIN"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/admin/user-management/user-management-dialog.html",
                            controller: "UserManagementDialogController",
                            size: "lg",
                            resolve: {
                                entity: [
                                    "User",
                                    function (User) {
                                        return User.get({
                                            login: $stateParams.login,
                                        });
                                    },
                                ],
                            },
                        })
                        .result.then(
                            function (result) {
                                $state.go("user-management", null, {
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
        .state("user-management.edit-password", {
            parent: "user-management",
            url: "/{id}/edit-password",
            data: {
                authorities: ["ROLE_ADMIN"],
            },
            onEnter: [
                "$stateParams",
                "$state",
                "$modal",
                function ($stateParams, $state, $modal) {
                    $modal
                        .open({
                            templateUrl:
                                "scripts/app/admin/user-management/user-management-edit-password.html",
                            controller: "UserManagementEditPasswordController",
                            size: "lg",
                            resolve: {},
                        })
                        .result.then(
                            function (result) {
                                $state.go("user-management", null, {
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
