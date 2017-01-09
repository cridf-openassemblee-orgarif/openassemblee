'use strict';

angular.module('babylone14166App').controller('SeanceDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Seance', 'PresenceElu',
        function($scope, $stateParams, $modalInstance, entity, Seance, PresenceElu) {

        $scope.seance = entity;
        $scope.presenceelus = PresenceElu.query();
        $scope.load = function(id) {
            Seance.get({id : id}, function(result) {
                $scope.seance = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('babylone14166App:seanceUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.seance.id != null) {
                Seance.update($scope.seance, onSaveSuccess, onSaveError);
            } else {
                Seance.save($scope.seance, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
