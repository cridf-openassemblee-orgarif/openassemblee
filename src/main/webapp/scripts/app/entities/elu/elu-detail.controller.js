'use strict';

angular.module('babylone14166App')
    .controller('EluDetailController', function ($scope, $rootScope, $stateParams, entity) {
        $scope.dto = entity;
        $scope.eluEnCommissionPermanente = false;
        $scope.eluEnCommissionPermanenteOuFonction = false;
        $scope.eluAGroupePolitique = false;
        $scope.eluAGroupePolitiqueOuFonction = false;
        $scope.groupePolitiqueId;
        $scope.eluACommissionThematique = false;
        $scope.$watch('dto', function () {
            if ($scope.dto.$promise) {
                $scope.dto.$promise.then(function () {
                    updateScope();
                });
            } else if ($scope.dto.elu) {
                updateScope();
            }
        });
        var updateScope = function () {
            $scope.eluEnCommissionPermanente = check('appartenancesCommissionPermanente');
            $scope.eluEnCommissionPermanenteOuFonction = check('appartenancesCommissionPermanente', 'fonctionsExecutives',
                'fonctionsCommissionPermanente');
            $scope.eluAGroupePolitique = check('appartenancesGroupePolitique');
            $scope.eluAGroupePolitiqueOuFonction = check('appartenancesGroupePolitique', 'fonctionsGroupePolitique');
            $scope.eluACommissionThematique = check('appartenancesCommissionsThematiques',
                'fonctionsCommissionsThematiques');
            $scope.eluAOrganismes = check('appartenancesOrganismes');
            angular.forEach($scope.dto.elu.appartenancesGroupePolitique, function (a) {
                if (a.dateFin == null) {
                    $scope.groupePolitiqueId = a.groupePolitique.id;
                    return;
                }
            });
        }
        var check = function () {
            var result = false;
            angular.forEach(arguments, function (gp) {
                angular.forEach($scope.dto.elu[gp], function (a) {
                    if (a.dateFin == null) {
                        result = true;
                    }
                });
            });
            return result;
        }
    });
