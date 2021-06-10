"use strict";

angular
    .module("openassembleeApp")
    .factory("MandatSearch", function ($resource) {
        return $resource(
            "api/_search/mandats/:query",
            {},
            {
                query: { method: "GET", isArray: true },
            }
        );
    });
