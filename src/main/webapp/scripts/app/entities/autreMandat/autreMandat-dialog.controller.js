'use strict';

angular.module('babylone14166App').controller('AutreMandatDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'AutreMandat', 'Elu',
        function($scope, $stateParams, $modalInstance, entity, AutreMandat, Elu) {

        $scope.autreMandat = entity;
        $scope.elus = Elu.query();
        $scope.load = function(id) {
            AutreMandat.get({id : id}, function(result) {
                $scope.autreMandat = result;
            });
        };

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
