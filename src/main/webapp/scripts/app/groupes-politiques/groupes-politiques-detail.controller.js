'use strict';

angular.module('babylone14166App')
    .controller('GroupePolitiqueDetailController', function ($scope, $rootScope, $stateParams, entity, GroupePolitique) {
        $scope.dto = entity;
        $scope.load = function (id) {
            GroupePolitique.get({id: id}, function(result) {
                $scope.dto = result;
            });
        };
        var unsubscribe = $rootScope.$on('babylone14166App:groupePolitiqueUpdate', function(event, result) {
            $scope.dto = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
