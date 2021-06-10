"use strict";

angular
    .module("openassembleeApp")
    .factory("DistinctionHonorifiqueSearch", function ($resource) {
        return $resource(
            "api/_search/distinctionHonorifiques/:query",
            {},
            {
                query: { method: "GET", isArray: true },
            }
        );
    });
