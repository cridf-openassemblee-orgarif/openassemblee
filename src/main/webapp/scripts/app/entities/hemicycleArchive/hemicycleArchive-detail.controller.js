'use strict';

angular.module('openassembleeApp')
    .controller('HemicycleArchiveDetailController', function ($scope, $rootScope, $stateParams, entity, HemicycleArchive) {
        $scope.hemicycleArchive = entity;
        $scope.load = function (id) {
            HemicycleArchive.get({id: id}, function(result) {
                $scope.hemicycleArchive = result;
            });
        };
        var unsubscribe = $rootScope.$on('openassembleeApp:hemicycleArchiveUpdate', function(event, result) {
            $scope.hemicycleArchive = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
