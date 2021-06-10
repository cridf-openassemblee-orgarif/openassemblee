"use strict";

angular
    .module("openassembleeApp")
    .controller(
        "PresenceEluDeleteController",
        function ($scope, $modalInstance, entity, PresenceElu) {
            $scope.presenceElu = entity;
            $scope.clear = function () {
                $modalInstance.dismiss("cancel");
            };
            $scope.confirmDelete = function (id) {
                PresenceElu.delete({ id: id }, function () {
                    $modalInstance.close(true);
                });
            };
        }
    );
