'use strict';

angular.module('babylone14166App')
    .controller('CommissionThematiqueDetailController', function ($scope, $rootScope, $stateParams, entity, CommissionThematique) {
        $scope.dto = entity;
        $scope.load = function (id) {
            CommissionThematique.get({id: id}, function(result) {
                $scope.dto = result;
            });
        };
        var unsubscribe = $rootScope.$on('babylone14166App:commissionThematiqueUpdate', function(event, result) {
            $scope.dto = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
