"use strict";

angular
    .module("openassembleeApp")
    .controller(
        "OrganismeDeleteController",
        function ($scope, $modalInstance, entity, Organisme) {
            $scope.organisme = entity;
            $scope.clear = function () {
                $modalInstance.dismiss("cancel");
            };
            $scope.confirmDelete = function (id) {
                Organisme.delete({ id: id }, function () {
                    $modalInstance.close(true);
                });
            };
        }
    );
