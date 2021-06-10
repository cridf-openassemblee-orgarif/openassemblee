"use strict";

angular
    .module("openassembleeApp")
    .factory("SeanceSearch", function ($resource) {
        return $resource(
            "api/_search/seances/:query",
            {},
            {
                query: { method: "GET", isArray: true },
            }
        );
    });
