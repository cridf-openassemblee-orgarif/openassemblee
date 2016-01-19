'use strict';

angular.module('babylone14166App').controller('EluDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Elu',
        function ($scope, $stateParams, $modalInstance, entity, Elu) {

        $scope.elu = entity;
        $scope.load = function(id) {
            Elu.get({id : id}, function(result) {
                $scope.elu = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('babylone14166App:eluUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.elu.id != null) {
                Elu.update($scope.elu, onSaveSuccess, onSaveError);
            } else {
                Elu.save($scope.elu, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
