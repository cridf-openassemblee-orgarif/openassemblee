"use strict";

angular
    .module("openassembleeApp")
    .controller(
        "FonctionCommissionPermanenteDeleteController",
        function (
            $scope,
            $modalInstance,
            entity,
            FonctionCommissionPermanente
        ) {
            $scope.appartenanceGroupePolitique = entity;
            $scope.clear = function () {
                $modalInstance.dismiss("cancel");
            };
            $scope.confirmDelete = function (id) {
                FonctionCommissionPermanente.delete({ id: id }, function () {
                    $modalInstance.close(true);
                });
            };
        }
    );
