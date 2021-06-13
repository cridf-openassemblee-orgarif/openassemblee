"use strict";

angular
    .module("openassembleeApp")
    .controller(
        "MandatureMakeCurrentController",
        function ($scope, Principal, $modalInstance, entity, Mandature, $http) {
            $scope.mandature = entity;
            $scope.clear = function () {
                $modalInstance.dismiss("cancel");
            };
            $scope.confirmCurrent = function (id) {
                $http
                    .post("api/mandatures/set-current/" + id)
                    .success(function () {
                        $http
                            .post(
                                "api/mandatures/set-current-for-session/" + id
                            )
                            .success(function () {
                                Principal.identity(true);
                                $modalInstance.close(true);
                            });
                    });
            };
        }
    );
