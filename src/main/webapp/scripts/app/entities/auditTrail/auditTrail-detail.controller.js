'use strict';

angular.module('babylone14166App')
    .controller('AuditTrailDetailController', function ($scope, $rootScope, $stateParams, entity, AuditTrail) {
        $scope.auditTrail = entity;

        var jsonDetail = function() {
            var obj = JSON.parse($scope.auditTrail.details);
            Object.keys(obj).forEach(function(k) {
                if(typeof obj[k] === 'object') {
                    obj[k] = {
                        id: obj[k].id
                    }
                }
            });
            $scope.json = obj;
            $scope.stringifiedJson = JSON.stringify(obj, null, "\t");
        };

        if(entity.$promise) {
            entity.$promise.then(function callback() {
                jsonDetail();
            });
        } else {
            jsonDetail();
        }
        $scope.load = function (id) {
            AuditTrail.get({id: id}, function(result) {
                $scope.auditTrail = result;
                jsonDetail();
            });
        };
        var unsubscribe = $rootScope.$on('babylone14166App:auditTrailUpdate', function(event, result) {
            $scope.auditTrail = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
