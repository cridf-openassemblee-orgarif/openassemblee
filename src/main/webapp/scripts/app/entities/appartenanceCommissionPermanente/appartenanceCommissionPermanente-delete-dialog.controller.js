"use strict";

angular
    .module("openassembleeApp")
    .controller(
        "AppartenanceCommissionPermanenteDeleteController",
        function (
            $scope,
            $modalInstance,
            entity,
            AppartenanceCommissionPermanente
        ) {
            $scope.appartenanceGroupePolitique = entity;
            $scope.clear = function () {
                $modalInstance.dismiss("cancel");
            };
            $scope.confirmDelete = function (id) {
                AppartenanceCommissionPermanente.delete(
                    { id: id },
                    function () {
                        $modalInstance.close(true);
                    }
                );
            };
        }
    );
