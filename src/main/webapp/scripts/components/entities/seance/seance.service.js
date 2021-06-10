"use strict";

angular
    .module("openassembleeApp")
    .factory("Seance", function ($resource, DateUtils) {
        return $resource(
            "api/seances/:id",
            {},
            {
                query: { method: "GET", isArray: true },
                get: {
                    method: "GET",
                    transformResponse: function (data) {
                        data = angular.fromJson(data);
                        data.date = DateUtils.convertLocaleDateFromServer(
                            data.date
                        );
                        return data;
                    },
                },
                getDto: {
                    method: "GET",
                    url: "api/seances/:id/dto",
                    transformResponse: function (data) {
                        data = angular.fromJson(data);
                        data.seance.date =
                            DateUtils.convertLocaleDateFromServer(
                                data.seance.date
                            );
                        data.pouvoirs.map(function (p) {
                            p.dateDebut = DateUtils.convertLocaleDateFromServer(
                                p.dateDebut
                            );
                            p.dateFin = DateUtils.convertLocaleDateFromServer(
                                p.dateFin
                            );
                            return p;
                        });
                        return data;
                    },
                },
                update: {
                    method: "PUT",
                    transformRequest: function (data) {
                        data.date = DateUtils.convertLocaleDateToServer(
                            data.date
                        );
                        return angular.toJson(data);
                    },
                },
                save: {
                    method: "POST",
                    transformRequest: function (data) {
                        data.date = DateUtils.convertLocaleDateToServer(
                            data.date
                        );
                        return angular.toJson(data);
                    },
                },
            }
        );
    });
