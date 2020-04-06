'use strict';

angular.module('openassembleeApp').controller('HemicycleConfigurationDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'HemicycleConfiguration',
        function($scope, $stateParams, $modalInstance, entity, HemicycleConfiguration) {

        $scope.hemicycleConfiguration = entity;
        $scope.load = function(id) {
            HemicycleConfiguration.get({id : id}, function(result) {
                $scope.hemicycleConfiguration = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('openassembleeApp:hemicycleConfigurationUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.hemicycleConfiguration.id != null) {
                HemicycleConfiguration.update($scope.hemicycleConfiguration, onSaveSuccess, onSaveError);
            } else {
                HemicycleConfiguration.save($scope.hemicycleConfiguration, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
