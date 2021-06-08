'use strict';

angular.module('openassembleeApp')
    .controller('ListeElectoraleDetailController', function ($scope, $rootScope, $stateParams, entity, ListeElectorale, Mandature) {
        $scope.listeElectorale = entity;
        $scope.load = function (id) {
            ListeElectorale.get({id: id}, function(result) {
                $scope.listeElectorale = result;
            });
        };
        var unsubscribe = $rootScope.$on('openassembleeApp:listeElectoraleUpdate', function(event, result) {
            $scope.listeElectorale = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
