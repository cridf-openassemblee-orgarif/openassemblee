'use strict';

angular.module('openassembleeApp')
    .controller('HemicycleConfigurationDetailController', function ($scope, $rootScope, $stateParams, entity, HemicycleConfiguration) {
        $scope.hemicycleConfiguration = entity;
        $scope.load = function (id) {
            HemicycleConfiguration.get({id: id}, function(result) {
                $scope.hemicycleConfiguration = result;
            });
        };
        var unsubscribe = $rootScope.$on('openassembleeApp:hemicycleConfigurationUpdate', function(event, result) {
            $scope.hemicycleConfiguration = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
