"use strict";

angular
    .module("openassembleeApp")
    .controller(
        "FonctionExecutiveDeleteController",
        function ($scope, $modalInstance, entity, FonctionExecutive) {
            $scope.fonctionCommissionThematique = entity;
            $scope.clear = function () {
                $modalInstance.dismiss("cancel");
            };
            $scope.confirmDelete = function (id) {
                FonctionExecutive.delete({ id: id }, function () {
                    $modalInstance.close(true);
                });
            };
        }
    );
