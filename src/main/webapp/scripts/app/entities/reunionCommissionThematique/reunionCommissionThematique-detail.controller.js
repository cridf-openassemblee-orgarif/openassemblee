'use strict';

angular.module('babylone14166App')
    .controller('ReunionCommissionThematiqueDetailController', function ($scope, $rootScope, $stateParams, entity, ReunionCommissionThematique) {
        $scope.reunion = entity;
        $scope.load = function (id) {
            ReunionCommissionThematique.get({id: id}, function(result) {
                $scope.reunion = result;
            });
        };
        var unsubscribe = $rootScope.$on('babylone14166App:reunionCommissionThematiqueUpdate', function(event, result) {
            $scope.reunion = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
