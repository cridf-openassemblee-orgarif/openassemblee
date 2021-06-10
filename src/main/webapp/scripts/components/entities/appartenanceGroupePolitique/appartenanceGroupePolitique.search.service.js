"use strict";

angular
    .module("openassembleeApp")
    .factory("AppartenanceGroupePolitiqueSearch", function ($resource) {
        return $resource(
            "api/_search/appartenanceGroupePolitiques/:query",
            {},
            {
                query: { method: "GET", isArray: true },
            }
        );
    });
