'use strict';

angular.module('babylone14166App').controller('NumeroFaxDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'NumeroFax', 'Elu',
        function ($scope, $stateParams, $modalInstance, entity, NumeroFax, Elu) {

        $scope.numeroFax = entity;
        $scope.load = function(id) {
            NumeroFax.get({id : id}, function(result) {
                $scope.numeroFax = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('babylone14166App:numeroFaxUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.numeroFax.id != null) {
                Elu.updateNumeroFax({id: $stateParams.id}, $scope.numeroFax, onSaveSuccess, onSaveError);
            } else {
                Elu.saveNumeroFax({id: $stateParams.id}, $scope.numeroFax, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
