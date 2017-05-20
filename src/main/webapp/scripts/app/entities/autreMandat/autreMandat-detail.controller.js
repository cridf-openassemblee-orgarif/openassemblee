'use strict';

angular.module('openassembleeApp')
    .controller('AutreMandatDetailController', function ($scope, $rootScope, $stateParams, entity, AutreMandat, Elu) {
        $scope.autreMandat = entity;
        $scope.load = function (id) {
            AutreMandat.get({id: id}, function(result) {
                $scope.autreMandat = result;
            });
        };
        var unsubscribe = $rootScope.$on('openassembleeApp:autreMandatUpdate', function(event, result) {
            $scope.autreMandat = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
