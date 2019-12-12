'use strict';

angular.module('openassembleeApp')
.controller('ExecutifController', function ($scope, entity) {
    $scope.comm = entity;

    $scope.$watch('comm', function () {
        $scope.comm.$promise.then(function (comm) {
            $scope.fonctions = comm.fonctions.filter(function (f) {
                return !f.dateFin
            });
            $scope.fonctionsExecutives = comm.fonctionsExecutives.filter(function (f) {
                return !f.dateFin
            });
        });
    });
});
