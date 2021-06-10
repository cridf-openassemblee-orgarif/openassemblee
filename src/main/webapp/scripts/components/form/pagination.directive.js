/* globals $ */
"use strict";

angular
    .module("openassembleeApp")
    .directive("openassembleeAppPagination", function () {
        return {
            templateUrl: "scripts/components/form/pagination.html",
        };
    });
