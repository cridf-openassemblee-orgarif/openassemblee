"use strict";

angular
    .module("openassembleeApp")
    .controller(
        "GroupePolitiqueDeleteController",
        function ($scope, $modalInstance, entity, GroupePolitique) {
            $scope.groupePolitique = entity;
            $scope.clear = function () {
                $modalInstance.dismiss("cancel");
            };
            $scope.confirmDelete = function (id) {
                GroupePolitique.delete({ id: id }, function () {
                    $modalInstance.close(true);
                });
            };
        }
    );
