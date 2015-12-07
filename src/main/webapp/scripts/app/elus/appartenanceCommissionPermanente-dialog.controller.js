'use strict';

angular.module('babylone14166App').controller('AppartenanceCommissionPermanenteDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'AppartenanceCommissionPermanente', 'Elu',
        function($scope, $stateParams, $modalInstance, entity, AppartenanceCommissionPermanente, Elu) {

        $scope.appartenanceCommissionPermanente = entity;
        $scope.elus = Elu.query();
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
            $scope.appartenanceCommissionPermanente.elu = {id: $stateParams.id};
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
