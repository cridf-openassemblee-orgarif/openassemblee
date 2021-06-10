"use strict";

angular
    .module("openassembleeApp")
    .factory("HemicycleConfigurationSearch", function ($resource) {
        return $resource(
            "api/_search/hemicycleConfigurations/:query",
            {},
            {
                query: { method: "GET", isArray: true },
            }
        );
    });
