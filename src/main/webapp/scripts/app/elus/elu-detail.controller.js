'use strict';

angular.module('babylone14166App')
    .controller('EluDetailController', function ($scope, $rootScope, $stateParams, entity) {
        $scope.r = entity;
        $scope.eluEnCommissionPermanente = false;
        $scope.$watch('r', function () {
            if ($scope.r.$promise) {
                $scope.r.$promise.then(function () {
                    updateScope();
                });
            } else if ($scope.r.elu) {
                updateScope();
            }
        });
        var updateScope = function () {
            $scope.elu = $scope.r.elu;
            $scope.groupesPolitiques = $scope.r.groupesPolitiques;
            $scope.commissionsThematiques = $scope.r.commissionsThematiques;
            $scope.organismes = $scope.r.organismes;
            if ($scope.elu.appartenancesCommissionPermanente &&
                $scope.elu.appartenancesCommissionPermanente.length > 0) {
                angular.forEach($scope.elu.appartenancesCommissionPermanente, function (a) {
                    if (a.dateFin == null) {
                        $scope.eluEnCommissionPermanente = true;
                    }
                });
            }
        }
    });
