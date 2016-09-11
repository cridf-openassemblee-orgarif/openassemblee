'use strict';

angular.module('babylone14166App')
    .controller('PouvoirDetailController', function ($scope, $rootScope, $stateParams, entity, Pouvoir, Elu) {
        $scope.pouvoir = entity;
        $scope.load = function (id) {
            Pouvoir.get({id: id}, function(result) {
                $scope.pouvoir = result;
            });
        };
        var unsubscribe = $rootScope.$on('babylone14166App:pouvoirUpdate', function(event, result) {
            $scope.pouvoir = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
