"use strict";

angular.module("openassembleeApp").config(function ($stateProvider) {
    $stateProvider.state("account", {
        abstract: true,
        parent: "site",
    });
});
