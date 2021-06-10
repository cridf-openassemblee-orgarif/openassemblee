"use strict";

angular
    .module("openassembleeApp")
    .controller(
        "EluDeleteController",
        function ($scope, $modalInstance, entity, Elu) {
            $scope.elu = entity;
            $scope.clear = function () {
                $modalInstance.dismiss("cancel");
            };
            $scope.confirmDelete = function (id) {
                Elu.delete({ id: id }, function () {
                    $modalInstance.close(true);
                });
            };
        }
    );
