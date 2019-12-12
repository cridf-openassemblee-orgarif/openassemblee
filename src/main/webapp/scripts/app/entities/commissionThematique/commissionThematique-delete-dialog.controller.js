'use strict';

angular.module('openassembleeApp')
    .controller('CommissionThematiqueDeleteController', function ($scope, $modalInstance, entity, CommissionThematique) {

        $scope.commissionThematique = entity;
        $scope.error = false;
        $scope.clear = function () {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            CommissionThematique.delete({id: $scope.commissionThematique.commissionThematique.id},
                function () {
                    $modalInstance.close(true);
                },
                function () {
                    $scope.error = true;
                });
        };

    });
