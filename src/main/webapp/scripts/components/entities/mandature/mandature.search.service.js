"use strict";

angular
    .module("openassembleeApp")
    .factory("MandatureSearch", function ($resource) {
        return $resource(
            "api/_search/mandatures/:query",
            {},
            {
                query: { method: "GET", isArray: true },
            }
        );
    });
