'use strict';

angular.module('babylone14166App')
    .controller('EluDetailController', function ($scope, $rootScope, $stateParams, entity) {
        $scope.dto = entity;
        $scope.eluEnCommissionPermanente = false;
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
            $scope.elu = $scope.dto.elu;
            if ($scope.dto.elu.appartenancesCommissionPermanente &&
                $scope.dto.elu.appartenancesCommissionPermanente.length > 0) {
                angular.forEach($scope.dto.elu.appartenancesCommissionPermanente, function (a) {
                    if (a.dateFin == null) {
                        $scope.eluEnCommissionPermanente = true;
                    }
                });
            }
        }
    });
