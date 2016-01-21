'use strict';

angular.module('babylone14166App').controller('IdentiteInternetDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'IdentiteInternet', 'Elu',
        function ($scope, $stateParams, $modalInstance, entity, IdentiteInternet, Elu) {

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
            Elu.saveIdentiteInternet({id: $stateParams.id}, $scope.identiteInternet, onSaveSuccess, onSaveError);
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
