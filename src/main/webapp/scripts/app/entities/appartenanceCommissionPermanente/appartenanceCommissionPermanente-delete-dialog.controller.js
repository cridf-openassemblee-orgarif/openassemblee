'use strict';

angular.module('babylone14166App')
	.controller('AppartenanceCommissionPermanenteDeleteController', function($scope, $modalInstance, entity, AppartenanceCommissionPermanente) {

        $scope.appartenanceCommissionPermanente = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            AppartenanceCommissionPermanente.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });