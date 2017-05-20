'use strict';

angular.module('openassembleeApp')
    .controller('AppartenanceOrganismeDetailController', function ($scope, $rootScope, $stateParams, entity, AppartenanceOrganisme, Elu) {
        $scope.appartenanceOrganisme = entity;
        $scope.load = function (id) {
            AppartenanceOrganisme.get({id: id}, function(result) {
                $scope.appartenanceOrganisme = result;
            });
        };
        var unsubscribe = $rootScope.$on('openassembleeApp:appartenanceOrganismeUpdate', function(event, result) {
            $scope.appartenanceOrganisme = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
