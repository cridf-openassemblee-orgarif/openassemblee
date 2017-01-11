'use strict';

angular.module('babylone14166App').controller('ReunionCaoDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'ReunionCao',
        function ($scope, $stateParams, $modalInstance, entity, ReunionCao) {

            $scope.reunionCao = entity;
            $scope.load = function (id) {
                ReunionCao.get({id: id}, function (result) {
                    $scope.reunionCao = result;
                });
            };

            var onSaveSuccess = function (result) {
                $scope.$emit('babylone14166App:reunionCaoUpdate', result);
                $modalInstance.close(result);
                $scope.isSaving = false;
            };

            var onSaveError = function (result) {
                $scope.isSaving = false;
            };

            $scope.save = function () {
                $scope.isSaving = true;
                if ($scope.reunionCao.id != null) {
                    ReunionCao.update($scope.reunionCao, onSaveSuccess, onSaveError);
                } else {
                    ReunionCao.save($scope.reunionCao, onSaveSuccess, onSaveError);
                }
            };

            $scope.clear = function () {
                $modalInstance.dismiss('cancel');
            };
        }]);
