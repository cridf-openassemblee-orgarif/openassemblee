"use strict";

angular
    .module("openassembleeApp")
    .factory("AutreMandatSearch", function ($resource) {
        return $resource(
            "api/_search/autreMandats/:query",
            {},
            {
                query: { method: "GET", isArray: true },
            }
        );
    });
