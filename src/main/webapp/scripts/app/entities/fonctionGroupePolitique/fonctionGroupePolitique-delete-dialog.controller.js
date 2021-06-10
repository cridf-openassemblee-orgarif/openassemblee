"use strict";

angular
    .module("openassembleeApp")
    .controller(
        "FonctionGroupePolitiqueDeleteController",
        function ($scope, $modalInstance, entity, FonctionGroupePolitique) {
            $scope.appartenanceGroupePolitique = entity;
            $scope.clear = function () {
                $modalInstance.dismiss("cancel");
            };
            $scope.confirmDelete = function (id) {
                FonctionGroupePolitique.delete({ id: id }, function () {
                    $modalInstance.close(true);
                });
            };
        }
    );
