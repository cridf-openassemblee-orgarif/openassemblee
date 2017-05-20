'use strict';

angular.module('openassembleeApp')
    .controller('ReunionCaoDetailController', function ($scope, $rootScope, $stateParams, entity, ReunionCao) {
        $scope.reunion = entity;
        $scope.load = function (id) {
            ReunionCao.get({id: id}, function(result) {
                $scope.reunion = result;
            });
        };
        var unsubscribe = $rootScope.$on('openassembleeApp:reunionCaoUpdate', function(event, result) {
            $scope.reunion = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
