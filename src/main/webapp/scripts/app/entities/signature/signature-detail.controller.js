'use strict';

angular.module('babylone14166App')
    .controller('SignatureDetailController', function ($scope, $rootScope, $stateParams, entity, Signature, PresenceElu) {
        $scope.signature = entity;
        $scope.load = function (id) {
            Signature.get({id: id}, function(result) {
                $scope.signature = result;
            });
        };
        var unsubscribe = $rootScope.$on('babylone14166App:signatureUpdate', function(event, result) {
            $scope.signature = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
