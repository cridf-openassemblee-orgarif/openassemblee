'use strict';

angular.module('babylone14166App')
	.controller('AuditTrailDeleteController', function($scope, $modalInstance, entity, AuditTrail) {

        $scope.auditTrail = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            AuditTrail.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });