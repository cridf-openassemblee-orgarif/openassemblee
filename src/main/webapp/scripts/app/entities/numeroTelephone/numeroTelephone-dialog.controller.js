'use strict';

angular.module('babylone14166App').controller('NumeroTelephoneDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'NumeroTelephone',
        function($scope, $stateParams, $modalInstance, entity, NumeroTelephone) {

        $scope.numeroTelephone = entity;
        $scope.load = function(id) {
            NumeroTelephone.get({id : id}, function(result) {
                $scope.numeroTelephone = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('babylone14166App:numeroTelephoneUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.numeroTelephone.id != null) {
                NumeroTelephone.update($scope.numeroTelephone, onSaveSuccess, onSaveError);
            } else {
                NumeroTelephone.save($scope.numeroTelephone, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
