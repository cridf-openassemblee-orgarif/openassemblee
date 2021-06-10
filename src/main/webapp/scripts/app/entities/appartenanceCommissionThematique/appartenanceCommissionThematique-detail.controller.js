"use strict";

angular
    .module("openassembleeApp")
    .controller(
        "AppartenanceCommissionThematiqueDetailController",
        function (
            $scope,
            $rootScope,
            $stateParams,
            entity,
            AppartenanceCommissionThematique,
            Elu,
            CommissionThematique
        ) {
            $scope.appartenanceCommissionThematique = entity;
            $scope.load = function (id) {
                AppartenanceCommissionThematique.get(
                    { id: id },
                    function (result) {
                        $scope.appartenanceCommissionThematique = result;
                    }
                );
            };
            var unsubscribe = $rootScope.$on(
                "openassembleeApp:appartenanceCommissionThematiqueUpdate",
                function (event, result) {
                    $scope.appartenanceCommissionThematique = result;
                }
            );
            $scope.$on("$destroy", unsubscribe);
        }
    );
