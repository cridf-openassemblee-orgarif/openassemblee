'use strict';

angular.module('babylone14166App')
    .controller('AppartenanceCommissionPermanenteDetailController', function ($scope, $rootScope, $stateParams, entity, AppartenanceCommissionPermanente, Elu) {
        $scope.appartenanceCommissionPermanente = entity;
        $scope.load = function (id) {
            AppartenanceCommissionPermanente.get({id: id}, function(result) {
                $scope.appartenanceCommissionPermanente = result;
            });
        };
        var unsubscribe = $rootScope.$on('babylone14166App:appartenanceCommissionPermanenteUpdate', function(event, result) {
            $scope.appartenanceCommissionPermanente = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
