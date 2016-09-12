'use strict';

angular.module('babylone14166App').controller('AuditTrailDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'AuditTrail',
        function($scope, $stateParams, $modalInstance, entity, AuditTrail) {

        $scope.auditTrail = entity;
        $scope.load = function(id) {
            AuditTrail.get({id : id}, function(result) {
                $scope.auditTrail = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('babylone14166App:auditTrailUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.auditTrail.id != null) {
                AuditTrail.update($scope.auditTrail, onSaveSuccess, onSaveError);
            } else {
                AuditTrail.save($scope.auditTrail, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
