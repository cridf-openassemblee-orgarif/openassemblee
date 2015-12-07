'use strict';

angular.module('babylone14166App')
    .controller('FonctionCommissionPermanenteDetailController', function ($scope, $rootScope, $stateParams, entity, FonctionCommissionPermanente) {
        $scope.fonctionCommissionPermanente = entity;
        $scope.load = function (id) {
            FonctionCommissionPermanente.get({id: id}, function(result) {
                $scope.fonctionCommissionPermanente = result;
            });
        };
        var unsubscribe = $rootScope.$on('babylone14166App:fonctionCommissionPermanenteUpdate', function(event, result) {
            $scope.fonctionCommissionPermanente = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
