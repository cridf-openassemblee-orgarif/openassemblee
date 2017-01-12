'use strict';

angular.module('babylone14166App').controller('FonctionCommissionThematiqueDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'FonctionCommissionThematique', 'Elu', 'CommissionThematique',
        function ($scope, $stateParams, $modalInstance, entity, FonctionCommissionThematique, Elu, CommissionThematique) {

            $scope.fonctionCommissionThematique = entity;
            $scope.elus = Elu.query();
            $scope.commissionthematiques = CommissionThematique.query();
            $scope.load = function (id) {
                FonctionCommissionThematique.get({id: id}, function (result) {
                    $scope.fonctionCommissionThematique = result;
                });
            };

            var onSaveSuccess = function (result) {
                $scope.$emit('babylone14166App:fonctionCommissionThematiqueUpdate', result);
                $modalInstance.close(result);
                $scope.isSaving = false;
            };

            var onSaveError = function (result) {
                $scope.isSaving = false;
            };

            $scope.save = function () {
                $scope.isSaving = true;
                $scope.fonctionCommissionThematique.elu = {id: $stateParams.id};
                if ($scope.fonctionCommissionThematique.id != null) {
                    FonctionCommissionThematique.update($scope.fonctionCommissionThematique, onSaveSuccess, onSaveError);
                } else {
                    FonctionCommissionThematique.save($scope.fonctionCommissionThematique, onSaveSuccess, onSaveError);
                }
            };

            $scope.clear = function () {
                $modalInstance.dismiss('cancel');
            };
        }]);
