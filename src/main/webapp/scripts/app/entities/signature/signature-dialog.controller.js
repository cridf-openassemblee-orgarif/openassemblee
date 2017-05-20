'use strict';

angular.module('openassembleeApp').controller('SignatureDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Signature', 'PresenceElu',
        function($scope, $stateParams, $modalInstance, entity, Signature, PresenceElu) {

        $scope.signature = entity;
        $scope.presenceelus = PresenceElu.query();
        $scope.load = function(id) {
            Signature.get({id : id}, function(result) {
                $scope.signature = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('openassembleeApp:signatureUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.signature.id != null) {
                Signature.update($scope.signature, onSaveSuccess, onSaveError);
            } else {
                Signature.save($scope.signature, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
