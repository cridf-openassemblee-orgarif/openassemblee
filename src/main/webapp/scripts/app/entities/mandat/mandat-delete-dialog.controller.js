"use strict";

angular
    .module("openassembleeApp")
    .controller(
        "MandatDeleteController",
        function ($scope, $modalInstance, entity, Mandat) {
            $scope.mandat = entity;
            $scope.clear = function () {
                $modalInstance.dismiss("cancel");
            };
            $scope.confirmDelete = function (id) {
                Mandat.delete({ id: id }, function () {
                    $modalInstance.close(true);
                });
            };
        }
    );
