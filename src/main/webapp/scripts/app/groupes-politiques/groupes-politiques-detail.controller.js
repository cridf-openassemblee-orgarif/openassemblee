'use strict';

angular.module('babylone14166App')
    .controller('GroupePolitiqueDetailController', function ($scope, $rootScope, $stateParams, entity, GroupePolitique) {
        $scope.groupePolitique = entity;
        $scope.load = function (id) {
            GroupePolitique.get({id: id}, function(result) {
                $scope.groupePolitique = result;
            });
        };
        var unsubscribe = $rootScope.$on('babylone14166App:groupePolitiqueUpdate', function(event, result) {
            $scope.groupePolitique = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
