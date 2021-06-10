"use strict";

angular
    .module("openassembleeApp")
    .factory("HemicycleConfiguration", function ($resource, DateUtils) {
        return $resource(
            "api/hemicycleConfigurations/:id",
            {},
            {
                query: { method: "GET", isArray: true },
                get: {
                    method: "GET",
                    transformResponse: function (data) {
                        data = angular.fromJson(data);
                        data.creationDate = DateUtils.convertDateTimeFromServer(
                            data.creationDate
                        );
                        data.lastModificationDate =
                            DateUtils.convertDateTimeFromServer(
                                data.lastModificationDate
                            );
                        data.frozenDate = DateUtils.convertDateTimeFromServer(
                            data.frozenDate
                        );
                        return data;
                    },
                },
                update: { method: "PUT" },
            }
        );
    });
