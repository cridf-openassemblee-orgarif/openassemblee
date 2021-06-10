"use strict";

angular
    .module("openassembleeApp")
    .controller(
        "HemicyclePlanDeleteController",
        function ($scope, $modalInstance, entity, HemicyclePlan) {
            $scope.hemicyclePlan = entity;
            $scope.clear = function () {
                $modalInstance.dismiss("cancel");
            };
            $scope.confirmDelete = function (id) {
                HemicyclePlan.delete({ id: id }, function () {
                    $modalInstance.close(true);
                });
            };
        }
    );
