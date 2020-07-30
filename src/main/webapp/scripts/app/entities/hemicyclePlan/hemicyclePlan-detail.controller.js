'use strict';

angular.module('openassembleeApp')
    .controller('HemicyclePlanDetailController', function ($scope, $rootScope, $stateParams, entity, HemicyclePlan, HemicycleConfiguration, Seance) {
        $scope.hemicyclePlan = entity;
        $scope.load = function (id) {
            HemicyclePlan.get({id: id}, function(result) {
                $scope.hemicyclePlan = result;
            });
        };
        var unsubscribe = $rootScope.$on('openassembleeApp:hemicyclePlanUpdate', function(event, result) {
            $scope.hemicyclePlan = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
