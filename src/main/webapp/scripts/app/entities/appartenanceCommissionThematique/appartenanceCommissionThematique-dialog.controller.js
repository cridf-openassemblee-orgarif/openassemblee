'use strict';

angular.module('openassembleeApp').controller('AppartenanceCommissionThematiqueDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'AppartenanceCommissionThematique', 'Elu', 'CommissionThematique',
        function ($scope, $stateParams, $modalInstance, entity, AppartenanceCommissionThematique, Elu, CommissionThematique) {

            $scope.appartenanceCommissionThematique = entity;
            // FIXME vérifier
            //$scope.elus = Elu.query();
            $scope.commissionthematiques = CommissionThematique.query();
            $scope.load = function (id) {
                AppartenanceCommissionThematique.get({id: id}, function (result) {
                    $scope.appartenanceCommissionThematique = result;
                });
            };

            var onSaveSuccess = function (result) {
                $scope.$emit('openassembleeApp:appartenanceCommissionThematiqueUpdate', result);
                $modalInstance.close(result);
                $scope.isSaving = false;
            };

            var onSaveError = function (result) {
                $scope.isSaving = false;
            };

            $scope.save = function () {
                $scope.isSaving = true;
                $scope.appartenanceCommissionThematique.elu = {id: $stateParams.id};
                if ($scope.appartenanceCommissionThematique.id != null) {
                    AppartenanceCommissionThematique.update($scope.appartenanceCommissionThematique, onSaveSuccess, onSaveError);
                } else {
                    AppartenanceCommissionThematique.save($scope.appartenanceCommissionThematique, onSaveSuccess, onSaveError);
                }
            };

            $scope.clear = function () {
                $modalInstance.dismiss('cancel');
            };
        }]);
