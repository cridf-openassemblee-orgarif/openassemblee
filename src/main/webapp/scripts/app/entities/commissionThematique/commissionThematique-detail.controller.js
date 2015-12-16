'use strict';

angular.module('babylone14166App')
    .controller('CommissionThematiqueDetailController', function ($scope, $rootScope, $stateParams, entity, CommissionThematique) {
        $scope.commissionThematique = entity;
        $scope.load = function (id) {
            CommissionThematique.get({id: id}, function(result) {
                $scope.commissionThematique = result;
            });
        };
        var unsubscribe = $rootScope.$on('babylone14166App:commissionThematiqueUpdate', function(event, result) {
            $scope.commissionThematique = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
