'use strict';

angular.module('babylone14166App')
    .controller('AuditTrailDetailController', function ($scope, $rootScope, $stateParams, entity, AuditTrail) {
        $scope.auditTrail = entity;
        $scope.load = function (id) {
            AuditTrail.get({id: id}, function(result) {
                $scope.auditTrail = result;
            });
        };
        var unsubscribe = $rootScope.$on('babylone14166App:auditTrailUpdate', function(event, result) {
            $scope.auditTrail = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
