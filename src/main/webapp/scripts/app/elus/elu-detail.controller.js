'use strict';

angular.module('babylone14166App')
    .controller('EluDetailController', function ($scope, $rootScope, $stateParams, entity, Elu) {
        $scope.elu = entity;
        $scope.load = function (id) {
            Elu.get({id: id}, function (result) {
                $scope.elu = result;
            });
        };
        var unsubscribe = $rootScope.$on('babylone14166App:eluUpdate', function (event, result) {
            $scope.elu = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
