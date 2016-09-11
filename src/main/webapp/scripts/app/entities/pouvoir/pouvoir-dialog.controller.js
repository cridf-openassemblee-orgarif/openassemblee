'use strict';

angular.module('babylone14166App').controller('PouvoirDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Pouvoir', 'Elu',
        function($scope, $stateParams, $modalInstance, entity, Pouvoir, Elu) {

        $scope.pouvoir = entity;
        $scope.elus = Elu.query();
        $scope.load = function(id) {
            Pouvoir.get({id : id}, function(result) {
                $scope.pouvoir = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('babylone14166App:pouvoirUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.pouvoir.id != null) {
                Pouvoir.update($scope.pouvoir, onSaveSuccess, onSaveError);
            } else {
                Pouvoir.save($scope.pouvoir, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
