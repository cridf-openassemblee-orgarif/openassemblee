'use strict';

angular.module('openassembleeApp').controller('HemicycleArchiveDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'HemicycleArchive',
        function($scope, $stateParams, $modalInstance, entity, HemicycleArchive) {

        $scope.hemicycleArchive = entity;
        $scope.load = function(id) {
            HemicycleArchive.get({id : id}, function(result) {
                $scope.hemicycleArchive = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('openassembleeApp:hemicycleArchiveUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.hemicycleArchive.id != null) {
                HemicycleArchive.update($scope.hemicycleArchive, onSaveSuccess, onSaveError);
            } else {
                HemicycleArchive.save($scope.hemicycleArchive, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
