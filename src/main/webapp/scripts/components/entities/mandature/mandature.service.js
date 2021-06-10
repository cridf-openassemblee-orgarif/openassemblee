"use strict";

angular
    .module("openassembleeApp")
    .factory("Mandature", function ($resource, DateUtils) {
        return $resource(
            "api/mandatures/:id",
            {},
            {
                query: { method: "GET", isArray: true },
                get: {
                    method: "GET",
                    transformResponse: function (data) {
                        data = angular.fromJson(data);
                        data.dateDebut = DateUtils.convertLocaleDateFromServer(
                            data.dateDebut
                        );
                        return data;
                    },
                },
                update: {
                    method: "PUT",
                    transformRequest: function (data) {
                        data.dateDebut = DateUtils.convertLocaleDateToServer(
                            data.dateDebut
                        );
                        return angular.toJson(data);
                    },
                },
                save: {
                    method: "POST",
                    transformRequest: function (data) {
                        data.dateDebut = DateUtils.convertLocaleDateToServer(
                            data.dateDebut
                        );
                        return angular.toJson(data);
                    },
                },
            }
        );
    });
