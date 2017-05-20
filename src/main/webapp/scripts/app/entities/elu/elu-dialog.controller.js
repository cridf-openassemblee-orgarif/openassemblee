'use strict';

angular.module('openassembleeApp').controller('EluDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Elu',
        function ($scope, $stateParams, $modalInstance, entity, Elu) {

            $scope.dto = entity;
            $scope.load = function (id) {
                Elu.get({id: id}, function (result) {
                    $scope.dto = result;
                });
            };

            var onSaveSuccess = function (result) {
                $scope.$emit('openassembleeApp:eluUpdate', result);
                $modalInstance.close(result);
                $scope.isSaving = false;
            };

            var onSaveError = function (result) {
                $scope.isSaving = false;
            };

            $scope.save = function () {
                $scope.isSaving = true;
                if ($scope.dto.elu.id != null) {
                    Elu.update($scope.dto.elu, onSaveSuccess, onSaveError);
                } else {
                    Elu.save($scope.dto.elu, onSaveSuccess, onSaveError);
                }
            };

            $scope.clear = function () {
                $modalInstance.dismiss('cancel');
            };
        }]);
