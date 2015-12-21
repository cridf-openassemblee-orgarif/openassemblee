'use strict';

angular.module('babylone14166App').controller('OrganismeDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Organisme',
        function ($scope, $stateParams, $modalInstance, entity, Organisme) {

        $scope.organisme = entity;
        $scope.load = function(id) {
            Organisme.get({id : id}, function(result) {
                $scope.organisme = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('babylone14166App:organismeUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.organisme.id != null) {
                Organisme.update($scope.organisme, onSaveSuccess, onSaveError);
            } else {
                Organisme.save($scope.organisme, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
