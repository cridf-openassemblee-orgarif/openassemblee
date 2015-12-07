'use strict';

angular.module('babylone14166App')
	.controller('FonctionCommissionPermanenteDeleteController', function($scope, $modalInstance, entity, FonctionCommissionPermanente) {

        $scope.fonctionCommissionPermanente = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            FonctionCommissionPermanente.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });