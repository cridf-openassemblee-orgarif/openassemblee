"use strict";

angular
    .module("openassembleeApp")
    .controller(
        "PouvoirDetailController",
        function ($scope, $rootScope, $stateParams, entity, Pouvoir, Elu) {
            $scope.pouvoir = entity;
            $scope.load = function (id) {
                Pouvoir.get({ id: id }, function (result) {
                    $scope.pouvoir = result;
                });
            };
            var unsubscribe = $rootScope.$on(
                "openassembleeApp:pouvoirUpdate",
                function (event, result) {
                    $scope.pouvoir = result;
                }
            );
            $scope.$on("$destroy", unsubscribe);
        }
    );
