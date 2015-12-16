'use strict';

angular.module('babylone14166App').controller('CommissionThematiqueDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'CommissionThematique',
        function($scope, $stateParams, $modalInstance, entity, CommissionThematique) {

        $scope.commissionThematique = entity;
        $scope.load = function(id) {
            CommissionThematique.get({id : id}, function(result) {
                $scope.commissionThematique = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('babylone14166App:commissionThematiqueUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.commissionThematique.id != null) {
                CommissionThematique.update($scope.commissionThematique, onSaveSuccess, onSaveError);
            } else {
                CommissionThematique.save($scope.commissionThematique, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
