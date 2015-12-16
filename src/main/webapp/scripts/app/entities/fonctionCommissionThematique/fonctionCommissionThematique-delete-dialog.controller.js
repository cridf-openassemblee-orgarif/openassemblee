'use strict';

angular.module('babylone14166App')
	.controller('FonctionCommissionThematiqueDeleteController', function($scope, $modalInstance, entity, FonctionCommissionThematique) {

        $scope.fonctionCommissionThematique = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            FonctionCommissionThematique.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });