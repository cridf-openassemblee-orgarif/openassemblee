"use strict";

angular
    .module("openassembleeApp")
    .controller(
        "MandatDetailController",
        function (
            $scope,
            $rootScope,
            $stateParams,
            entity,
            Mandat,
            Elu,
            Mandature,
            ListeElectorale
        ) {
            $scope.mandat = entity;
            $scope.load = function (id) {
                Mandat.get({ id: id }, function (result) {
                    $scope.mandat = result;
                });
            };
            var unsubscribe = $rootScope.$on(
                "openassembleeApp:mandatUpdate",
                function (event, result) {
                    $scope.mandat = result;
                }
            );
            $scope.$on("$destroy", unsubscribe);
        }
    );
