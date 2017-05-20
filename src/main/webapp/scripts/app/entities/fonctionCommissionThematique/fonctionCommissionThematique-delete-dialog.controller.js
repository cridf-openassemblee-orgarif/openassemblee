'use strict';

angular.module('openassembleeApp')
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
