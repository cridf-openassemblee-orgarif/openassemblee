'use strict';

angular.module('babylone14166App')
    .controller('PresenceEluDetailController', function ($scope, $rootScope, $stateParams, entity, PresenceElu, Elu, Signature) {
        $scope.presenceElu = entity;
        $scope.load = function (id) {
            PresenceElu.get({id: id}, function(result) {
                $scope.presenceElu = result;
            });
        };
        var unsubscribe = $rootScope.$on('babylone14166App:presenceEluUpdate', function(event, result) {
            $scope.presenceElu = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
