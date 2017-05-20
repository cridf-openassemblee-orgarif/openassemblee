'use strict';

angular.module('openassembleeApp')
    .controller('PresenceEluDetailController', function ($scope, $rootScope, $stateParams, entity, PresenceElu, Elu, Signature, Seance) {
        $scope.presenceElu = entity;
        $scope.load = function (id) {
            PresenceElu.get({id: id}, function(result) {
                $scope.presenceElu = result;
            });
        };
        var unsubscribe = $rootScope.$on('openassembleeApp:presenceEluUpdate', function(event, result) {
            $scope.presenceElu = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
