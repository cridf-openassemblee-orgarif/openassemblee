"use strict";

angular.module("openassembleeApp").config(function ($stateProvider) {
    $stateProvider.state("incoherences", {
        parent: "admin",
        url: "/incoherences",
        data: {
            authorities: ["ROLE_ADMIN"],
            pageTitle: "Application inconsistencies",
        },
        views: {
            "content@": {
                templateUrl:
                    "scripts/app/admin/inconsistency/inconsistency.html",
                controller: "InconsistencyController",
            },
        },
        resolve: {},
    });
});
