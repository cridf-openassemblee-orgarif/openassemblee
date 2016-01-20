'use strict';

angular.module('babylone14166App').controller('NumeroFaxDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'NumeroFax',
        function($scope, $stateParams, $modalInstance, entity, NumeroFax) {

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
                NumeroFax.update($scope.numeroFax, onSaveSuccess, onSaveError);
            } else {
                NumeroFax.save($scope.numeroFax, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
