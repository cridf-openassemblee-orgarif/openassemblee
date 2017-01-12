'use strict';

angular.module('babylone14166App').controller('AutreMandatDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'title', 'entity', 'AutreMandat', 'Elu',
        function($scope, $stateParams, $modalInstance, title, entity, AutreMandat, Elu) {

        $scope.title = title;
        $scope.autreMandat = entity;

        var onSaveSuccess = function (result) {
            $scope.$emit('babylone14166App:autreMandatUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            $scope.autreMandat.elu = {id: $stateParams.id};
            if ($scope.autreMandat.id != null) {
                AutreMandat.update($scope.autreMandat, onSaveSuccess, onSaveError);
            } else {
                AutreMandat.save($scope.autreMandat, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
