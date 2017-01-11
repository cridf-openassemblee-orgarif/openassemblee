'use strict';

angular.module('babylone14166App')
	.controller('ReunionCommissionThematiqueDeleteController', function($scope, $modalInstance, entity, ReunionCommissionThematique) {

        $scope.reunionCommissionThematique = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            ReunionCommissionThematique.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });