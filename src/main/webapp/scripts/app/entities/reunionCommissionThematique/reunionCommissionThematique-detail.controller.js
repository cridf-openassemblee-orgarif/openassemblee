'use strict';

angular.module('babylone14166App')
    .controller('ReunionCommissionThematiqueDetailController', function ($scope, $rootScope, $stateParams, entity, ReunionCommissionThematique) {
        $scope.reunionCommissionThematique = entity;
        $scope.load = function (id) {
            ReunionCommissionThematique.get({id: id}, function(result) {
                $scope.reunionCommissionThematique = result;
            });
        };
        var unsubscribe = $rootScope.$on('babylone14166App:reunionCommissionThematiqueUpdate', function(event, result) {
            $scope.reunionCommissionThematique = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
