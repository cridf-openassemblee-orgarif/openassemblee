'use strict';

angular.module('babylone14166App').controller('AppartenanceCommissionPermanenteDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'AppartenanceCommissionPermanente',
        function($scope, $stateParams, $modalInstance, entity, AppartenanceCommissionPermanente) {

        $scope.appartenanceCommissionPermanente = entity;
        $scope.load = function(id) {
            AppartenanceCommissionPermanente.get({id : id}, function(result) {
                $scope.appartenanceCommissionPermanente = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('babylone14166App:appartenanceCommissionPermanenteUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.appartenanceCommissionPermanente.id != null) {
                AppartenanceCommissionPermanente.update($scope.appartenanceCommissionPermanente, onSaveSuccess, onSaveError);
            } else {
                AppartenanceCommissionPermanente.save($scope.appartenanceCommissionPermanente, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
