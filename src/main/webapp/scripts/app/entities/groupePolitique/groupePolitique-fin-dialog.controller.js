'use strict';

angular.module('babylone14166App').controller('GroupePolitiqueFinDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'GroupePolitique',
        function($scope, $stateParams, $modalInstance, entity, GroupePolitique) {

            $scope.dto = entity;
        $scope.load = function(id) {
            GroupePolitique.get({id : id}, function(result) {
                $scope.dto = result;
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
            GroupePolitique.update($scope.dto.groupePolitique, onSaveSuccess, onSaveError);
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
