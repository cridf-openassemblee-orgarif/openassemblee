'use strict';

angular.module('babylone14166App')
    .controller('OrganismeDetailController', function ($scope, $rootScope, $stateParams, entity, Organisme) {
        $scope.organisme = entity;
        $scope.load = function (id) {
            Organisme.get({id: id}, function(result) {
                $scope.organisme = result;
            });
        };
        var unsubscribe = $rootScope.$on('babylone14166App:organismeUpdate', function(event, result) {
            $scope.organisme = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
