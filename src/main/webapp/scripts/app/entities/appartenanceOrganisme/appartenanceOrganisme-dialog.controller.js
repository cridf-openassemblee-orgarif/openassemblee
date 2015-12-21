'use strict';

angular.module('babylone14166App').controller('AppartenanceOrganismeDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'AppartenanceOrganisme', 'Elu',
        function($scope, $stateParams, $modalInstance, entity, AppartenanceOrganisme, Elu) {

        $scope.appartenanceOrganisme = entity;
        $scope.elus = Elu.query();
        $scope.load = function(id) {
            AppartenanceOrganisme.get({id : id}, function(result) {
                $scope.appartenanceOrganisme = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('babylone14166App:appartenanceOrganismeUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.appartenanceOrganisme.id != null) {
                AppartenanceOrganisme.update($scope.appartenanceOrganisme, onSaveSuccess, onSaveError);
            } else {
                AppartenanceOrganisme.save($scope.appartenanceOrganisme, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
