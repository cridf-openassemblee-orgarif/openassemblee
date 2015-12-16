'use strict';

angular.module('babylone14166App')
    .controller('AppartenanceCommissionThematiqueDetailController', function ($scope, $rootScope, $stateParams, entity, AppartenanceCommissionThematique, Elu, CommissionThematique) {
        $scope.appartenanceCommissionThematique = entity;
        $scope.load = function (id) {
            AppartenanceCommissionThematique.get({id: id}, function(result) {
                $scope.appartenanceCommissionThematique = result;
            });
        };
        var unsubscribe = $rootScope.$on('babylone14166App:appartenanceCommissionThematiqueUpdate', function(event, result) {
            $scope.appartenanceCommissionThematique = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
