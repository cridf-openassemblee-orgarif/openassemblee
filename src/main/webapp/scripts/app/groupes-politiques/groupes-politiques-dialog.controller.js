'use strict';

angular.module('babylone14166App').controller('GroupePolitiqueDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'GroupePolitique',
        function($scope, $stateParams, $modalInstance, entity, GroupePolitique) {

        $scope.groupePolitique = entity;
        $scope.load = function(id) {
            GroupePolitique.get({id : id}, function(result) {
                $scope.groupePolitique = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('babylone14166App:groupePolitiqueUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.groupePolitique.id != null) {
                GroupePolitique.update($scope.groupePolitique, onSaveSuccess, onSaveError);
            } else {
                GroupePolitique.save($scope.groupePolitique, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
