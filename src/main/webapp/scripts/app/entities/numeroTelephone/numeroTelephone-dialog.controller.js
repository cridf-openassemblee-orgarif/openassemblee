'use strict';

angular.module('openassembleeApp').controller('NumeroTelephoneDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'NumeroTelephone', 'Elu',
        function ($scope, $stateParams, $modalInstance, entity, NumeroTelephone, Elu) {

        $scope.numeroTelephone = entity;
        $scope.load = function(id) {
            NumeroTelephone.get({id : id}, function(result) {
                $scope.numeroTelephone = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('openassembleeApp:numeroTelephoneUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.numeroTelephone.id != null) {
                Elu.updateNumeroTelephone({id: $stateParams.id}, $scope.numeroTelephone, onSaveSuccess, onSaveError);
            } else {
                Elu.saveNumeroTelephone({id: $stateParams.id}, $scope.numeroTelephone, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
