'use strict';

angular.module('openassembleeApp')
    .controller('CommissionThematiqueDetailController', function ($scope, $rootScope, $stateParams, entity, CommissionThematique) {
        $scope.dto = entity;
        $scope.appartenances = [];
        $scope.fonctions = [];
        $scope.appartenancesAnciens = [];
        $scope.fonctionsAnciens = [];

        $scope.$watch('dto', function () {
            $scope.dto.$promise.then(function (result) {
                $scope.dto = result;
                $scope.appartenances = result.appartenanceCommissionThematiqueDTOs.slice().filter(function (f) {
                    return !f.appartenanceCommissionThematique.dateFin;
                });
                $scope.fonctions = result.fonctionCommissionThematiqueDTOs.slice().filter(function (f) {
                    return !f.fonctionCommissionThematique.dateFin;
                });
                $scope.appartenancesAnciens = result.appartenanceCommissionThematiqueDTOs.slice().filter(function (f) {
                    return f.appartenanceCommissionThematique.dateFin;
                });
                $scope.fonctionsAnciens = result.fonctionCommissionThematiqueDTOs.slice().filter(function (f) {
                    return f.fonctionCommissionThematique.dateFin;
                });
            });
        });
    });
