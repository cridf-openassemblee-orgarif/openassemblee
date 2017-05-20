'use strict';

angular.module('openassembleeApp').controller('PresenceEluDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'PresenceElu', 'Elu', 'Signature', 'Seance',
        function($scope, $stateParams, $modalInstance, entity, PresenceElu, Elu, Signature, Seance) {

        $scope.presenceElu = entity;
        $scope.elus = Elu.query();
        $scope.signatures = Signature.query();
        $scope.seances = Seance.query();
        $scope.load = function(id) {
            PresenceElu.get({id : id}, function(result) {
                $scope.presenceElu = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('openassembleeApp:presenceEluUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.presenceElu.id != null) {
                PresenceElu.update($scope.presenceElu, onSaveSuccess, onSaveError);
            } else {
                PresenceElu.save($scope.presenceElu, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
