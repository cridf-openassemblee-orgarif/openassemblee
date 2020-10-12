'use strict';

angular.module('openassembleeApp')
    .controller('AuditTrailDetailController', function ($scope, $rootScope, $stateParams, entity, AuditTrail) {
        $scope.auditTrail = entity;

        var jsonDetail = function() {
            var obj = JSON.parse($scope.auditTrail.details);
            // TODO bon marche très partiellement... ne sert à rien s'il y a une collection par ex
            // et de toute façon on s'en fout avec un widget qui présente un tree
            Object.keys(obj).forEach(function(k) {
                if(typeof obj[k] === 'object' && obj[k] && obj[k].id) {
                    obj[k] = {
                        id: obj[k].id
                    }
                }
            });
            $scope.json = obj;
            $scope.dto = JSON.parse($scope.auditTrail.dto);
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
        var unsubscribe = $rootScope.$on('openassembleeApp:auditTrailUpdate', function(event, result) {
            $scope.auditTrail = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
