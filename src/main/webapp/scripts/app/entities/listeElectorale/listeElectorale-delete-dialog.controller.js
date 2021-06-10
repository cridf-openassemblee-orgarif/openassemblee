"use strict";

angular
    .module("openassembleeApp")
    .controller(
        "ListeElectoraleDeleteController",
        function ($scope, $modalInstance, entity, ListeElectorale) {
            $scope.listeElectorale = entity;
            $scope.clear = function () {
                $modalInstance.dismiss("cancel");
            };
            $scope.confirmDelete = function (id) {
                ListeElectorale.delete({ id: id }, function () {
                    $modalInstance.close(true);
                });
            };
        }
    );
