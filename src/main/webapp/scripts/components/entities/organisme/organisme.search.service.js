"use strict";

angular
    .module("openassembleeApp")
    .factory("OrganismeSearch", function ($resource) {
        return $resource(
            "api/_search/organismes/:query",
            {},
            {
                query: { method: "GET", isArray: true },
            }
        );
    });
