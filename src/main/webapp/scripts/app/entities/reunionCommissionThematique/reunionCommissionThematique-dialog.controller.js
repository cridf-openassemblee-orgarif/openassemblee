'use strict';

angular.module('babylone14166App').controller('ReunionCommissionThematiqueDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'ReunionCommissionThematique',
        function ($scope, $stateParams, $modalInstance, entity, ReunionCommissionThematique) {

            $scope.reunionCommissionThematique = entity;
            $scope.load = function (id) {
                ReunionCommissionThematique.get({id: id}, function (result) {
                    $scope.reunionCommissionThematique = result;
                });
            };

            var onSaveSuccess = function (result) {
                $scope.$emit('babylone14166App:reunionCommissionThematiqueUpdate', result);
                $modalInstance.close(result);
                $scope.isSaving = false;
            };

            var onSaveError = function (result) {
                $scope.isSaving = false;
            };

            $scope.save = function () {
                $scope.isSaving = true;
                if ($scope.reunionCommissionThematique.id != null) {
                    ReunionCommissionThematique.update($scope.reunionCommissionThematique, onSaveSuccess, onSaveError);
                } else {
                    ReunionCommissionThematique.save($scope.reunionCommissionThematique, onSaveSuccess, onSaveError);
                }
            };

            $scope.clear = function () {
                $modalInstance.dismiss('cancel');
            };
        }]);
