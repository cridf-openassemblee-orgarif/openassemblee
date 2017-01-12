'use strict';

angular.module('babylone14166App')
    .controller('ReunionCaoDetailController', function ($scope, $rootScope, $stateParams, entity, ReunionCao) {
        $scope.reunion = entity;
        $scope.load = function (id) {
            ReunionCao.get({id: id}, function(result) {
                $scope.reunion = result;
            });
        };
        var unsubscribe = $rootScope.$on('babylone14166App:reunionCaoUpdate', function(event, result) {
            $scope.reunion = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
