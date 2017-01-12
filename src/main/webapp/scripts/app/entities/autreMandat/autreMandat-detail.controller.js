'use strict';

angular.module('babylone14166App')
    .controller('AutreMandatDetailController', function ($scope, $rootScope, $stateParams, entity, AutreMandat, Elu) {
        $scope.autreMandat = entity;
        $scope.load = function (id) {
            AutreMandat.get({id: id}, function(result) {
                $scope.autreMandat = result;
            });
        };
        var unsubscribe = $rootScope.$on('babylone14166App:autreMandatUpdate', function(event, result) {
            $scope.autreMandat = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
