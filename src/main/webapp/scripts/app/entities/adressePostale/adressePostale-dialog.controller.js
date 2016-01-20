'use strict';

angular.module('babylone14166App').controller('AdressePostaleDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'AdressePostale',
        function($scope, $stateParams, $modalInstance, entity, AdressePostale) {

        $scope.adressePostale = entity;
        $scope.load = function(id) {
            AdressePostale.get({id : id}, function(result) {
                $scope.adressePostale = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('babylone14166App:adressePostaleUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.adressePostale.id != null) {
                AdressePostale.update($scope.adressePostale, onSaveSuccess, onSaveError);
            } else {
                AdressePostale.save($scope.adressePostale, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
