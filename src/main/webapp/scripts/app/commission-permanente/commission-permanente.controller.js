"use strict";

angular
    .module("openassembleeApp")
    .controller("CommissionPermanenteController", function ($scope, entity) {
        $scope.comm = entity;

        $scope.$watch("comm", function () {
            $scope.comm.$promise.then(function (comm) {
                $scope.appartenances = comm.appartenances.filter(function (f) {
                    return !f.dateFin;
                });
            });
        });
    });
