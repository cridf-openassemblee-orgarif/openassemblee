"use strict";

angular
    .module("openassembleeApp")
    .factory("FonctionCommissionPermanente", function ($resource, DateUtils) {
        return $resource(
            "api/fonctionCommissionPermanentes/:id",
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
                        data.dateFin = DateUtils.convertLocaleDateFromServer(
                            data.dateFin
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
                        data.dateFin = DateUtils.convertLocaleDateToServer(
                            data.dateFin
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
                        data.dateFin = DateUtils.convertLocaleDateToServer(
                            data.dateFin
                        );
                        return angular.toJson(data);
                    },
                },
            }
        );
    });
