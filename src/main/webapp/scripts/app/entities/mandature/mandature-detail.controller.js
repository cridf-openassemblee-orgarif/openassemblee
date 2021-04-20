'use strict';

angular.module('openassembleeApp')
    .controller('MandatureDetailController', function ($scope, $rootScope, $stateParams, entity, Mandature) {
        $scope.mandature = entity;
        $scope.load = function (id) {
            Mandature.get({id: id}, function(result) {
                $scope.mandature = result;
            });
        };
        var unsubscribe = $rootScope.$on('openassembleeApp:mandatureUpdate', function(event, result) {
            $scope.mandature = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
