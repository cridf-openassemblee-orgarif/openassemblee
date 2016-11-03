'use strict';

angular.module('babylone14166App')
    .controller('SeanceDetailController', function ($scope, $rootScope, $stateParams, entity, Seance) {
        $scope.dto = entity;
        $scope.load = function (id) {
            Seance.getDto({id: id}, function(result) {
                $scope.dto = result;
            });
        };
        var unsubscribe = $rootScope.$on('babylone14166App:seanceUpdate', function(event, result) {
            $scope.seance = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
