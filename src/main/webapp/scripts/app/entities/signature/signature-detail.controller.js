'use strict';

angular.module('openassembleeApp')
    .controller('SignatureDetailController', function ($scope, $rootScope, $stateParams, entity, Signature, PresenceElu) {
        $scope.signature = entity;
        $scope.load = function (id) {
            Signature.get({id: id}, function(result) {
                $scope.signature = result;
            });
        };
        var unsubscribe = $rootScope.$on('openassembleeApp:signatureUpdate', function(event, result) {
            $scope.signature = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
