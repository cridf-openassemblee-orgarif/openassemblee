"use strict";

angular
    .module("openassembleeApp")
    .controller(
        "PouvoirDeleteController",
        function ($scope, $modalInstance, entity, Pouvoir) {
            $scope.pouvoir = entity;
            $scope.clear = function () {
                $modalInstance.dismiss("cancel");
            };
            $scope.confirmDelete = function (id) {
                Pouvoir.delete({ id: id }, function () {
                    $modalInstance.close(true);
                });
            };
        }
    );
