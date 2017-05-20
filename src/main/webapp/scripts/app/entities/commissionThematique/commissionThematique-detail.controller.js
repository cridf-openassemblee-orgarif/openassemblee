'use strict';

angular.module('openassembleeApp')
    .controller('CommissionThematiqueDetailController', function ($scope, $rootScope, $stateParams, entity, CommissionThematique) {
        $scope.dto = entity;
        $scope.load = function (id) {
            CommissionThematique.get({id: id}, function(result) {
                $scope.dto = result;
            });
        };
        var unsubscribe = $rootScope.$on('openassembleeApp:commissionThematiqueUpdate', function(event, result) {
            $scope.dto = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
