'use strict';

angular.module('babylone14166App').controller('IdentiteInternetDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'IdentiteInternet',
        function($scope, $stateParams, $modalInstance, entity, IdentiteInternet) {

        $scope.identiteInternet = entity;
        $scope.load = function(id) {
            IdentiteInternet.get({id : id}, function(result) {
                $scope.identiteInternet = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('babylone14166App:identiteInternetUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.identiteInternet.id != null) {
                IdentiteInternet.update($scope.identiteInternet, onSaveSuccess, onSaveError);
            } else {
                IdentiteInternet.save($scope.identiteInternet, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
