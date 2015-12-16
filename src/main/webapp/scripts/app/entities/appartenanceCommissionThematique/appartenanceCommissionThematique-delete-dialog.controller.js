'use strict';

angular.module('babylone14166App')
	.controller('AppartenanceCommissionThematiqueDeleteController', function($scope, $modalInstance, entity, AppartenanceCommissionThematique) {

        $scope.appartenanceCommissionThematique = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            AppartenanceCommissionThematique.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });