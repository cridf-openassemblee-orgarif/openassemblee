'use strict';

angular.module('openassembleeApp').controller('HemicyclePlanDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'HemicyclePlan', 'HemicycleConfiguration', 'Seance',
        function($scope, $stateParams, $modalInstance, entity, HemicyclePlan, HemicycleConfiguration, Seance) {

        $scope.hemicyclePlan = entity;
        $scope.hemicycleconfigurations = HemicycleConfiguration.query();
        $scope.seances = Seance.query();
        $scope.load = function(id) {
            HemicyclePlan.get({id : id}, function(result) {
                $scope.hemicyclePlan = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('openassembleeApp:hemicyclePlanUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.hemicyclePlan.id != null) {
                HemicyclePlan.update($scope.hemicyclePlan, onSaveSuccess, onSaveError);
            } else {
                HemicyclePlan.save($scope.hemicyclePlan, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
