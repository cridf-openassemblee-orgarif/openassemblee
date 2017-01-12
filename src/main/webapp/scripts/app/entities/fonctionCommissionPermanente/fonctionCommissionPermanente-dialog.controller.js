'use strict';

angular.module('babylone14166App').controller('FonctionCommissionPermanenteDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'FonctionCommissionPermanente', 'Elu',
        function($scope, $stateParams, $modalInstance, entity, FonctionCommissionPermanente, Elu) {

        $scope.fonctionCommissionPermanente = entity;
            // FIXME vérifier
            //$scope.elus = Elu.query();
        $scope.load = function(id) {
            FonctionCommissionPermanente.get({id : id}, function(result) {
                $scope.fonctionCommissionPermanente = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('babylone14166App:fonctionCommissionPermanenteUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            $scope.fonctionCommissionPermanente.elu = {id: $stateParams.id};
            if ($scope.fonctionCommissionPermanente.id != null) {
                FonctionCommissionPermanente.update($scope.fonctionCommissionPermanente, onSaveSuccess, onSaveError);
            } else {
                FonctionCommissionPermanente.save($scope.fonctionCommissionPermanente, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
