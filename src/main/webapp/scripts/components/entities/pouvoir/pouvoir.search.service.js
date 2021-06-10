"use strict";

angular
    .module("openassembleeApp")
    .factory("PouvoirSearch", function ($resource) {
        return $resource(
            "api/_search/pouvoirs/:query",
            {},
            {
                query: { method: "GET", isArray: true },
            }
        );
    });
