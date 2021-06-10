"use strict";

angular
    .module("openassembleeApp")
    .factory("notificationInterceptor", function ($q, AlertService) {
        return {
            response: function (response) {
                var alertKey = response.headers("X-openassembleeApp-alert");
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, {
                        param: response.headers("X-openassembleeApp-params"),
                    });
                }
                return response;
            },
        };
    });
