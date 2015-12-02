'use strict';

angular.module('babylone14166App')
    .controller('AdressePostaleDetailController', function ($scope, $rootScope, $stateParams, entity, AdressePostale) {
        $scope.adressePostale = entity;
        $scope.load = function (id) {
            AdressePostale.get({id: id}, function(result) {
                $scope.adressePostale = result;
            });
        };
        var unsubscribe = $rootScope.$on('babylone14166App:adressePostaleUpdate', function(event, result) {
            $scope.adressePostale = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
