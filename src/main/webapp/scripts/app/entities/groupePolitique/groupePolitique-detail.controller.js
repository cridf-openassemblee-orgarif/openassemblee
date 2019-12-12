'use strict';

angular.module('openassembleeApp')
.controller('GroupePolitiqueDetailController', function ($scope, $rootScope, $stateParams, entity, GroupePolitique) {
    $scope.dto = entity;
    $scope.appartenances = [];
    $scope.fonctions = [];
    $scope.appartenancesAnciens = [];
    $scope.fonctionsAnciens = [];

    $scope.$watch('dto', function () {
        $scope.dto.$promise.then(function (result) {
            $scope.dto = result;
            $scope.appartenances = result.appartenanceGroupePolitiqueDTOs.slice().filter(function (f) {
                return !f.appartenanceGroupePolitique.dateFin;
            });
            $scope.fonctions = result.fonctionGroupePolitiqueDTOs.slice().filter(function (f) {
                return !f.fonctionGroupePolitique.dateFin;
            });
            $scope.appartenancesAnciens = result.appartenanceGroupePolitiqueDTOs.slice().filter(function (f) {
                return f.appartenanceGroupePolitique.dateFin;
            });
            $scope.fonctionsAnciens = result.fonctionGroupePolitiqueDTOs.slice().filter(function (f) {
                return f.fonctionGroupePolitique.dateFin;
            });
        });
    });

});
