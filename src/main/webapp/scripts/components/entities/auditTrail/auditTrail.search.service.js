"use strict";

angular
    .module("openassembleeApp")
    .factory("AuditTrailSearch", function ($resource) {
        return $resource(
            "api/_search/auditTrails/:query",
            {},
            {
                query: { method: "GET", isArray: true },
            }
        );
    });
