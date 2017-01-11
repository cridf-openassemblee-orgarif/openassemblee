'use strict';

angular.module('babylone14166App')
    .controller('ReunionCaoDetailController', function ($scope, $rootScope, $stateParams, entity, ReunionCao) {
        $scope.reunionCao = entity;
        $scope.load = function (id) {
            ReunionCao.get({id: id}, function(result) {
                $scope.reunionCao = result;
            });
        };
        var unsubscribe = $rootScope.$on('babylone14166App:reunionCaoUpdate', function(event, result) {
            $scope.reunionCao = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
