"use strict";

angular.module("openassembleeApp").factory("EluSearch", function ($resource) {
    return $resource(
        "api/_search/elus/:query",
        {},
        {
            query: { method: "GET", isArray: true },
        }
    );
});
