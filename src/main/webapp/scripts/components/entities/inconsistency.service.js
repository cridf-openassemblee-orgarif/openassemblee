"use strict";

angular
    .module("openassembleeApp")
    .factory("InconsistencyService", function ($http) {
        return {
            get: function () {
                return $http.get("api/inconsistency").then(function (response) {
                    return response.data;
                });
            },
        };
    });
