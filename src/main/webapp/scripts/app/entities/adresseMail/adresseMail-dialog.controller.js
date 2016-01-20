'use strict';

angular.module('babylone14166App').controller('AdresseMailDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'AdresseMail',
        function($scope, $stateParams, $modalInstance, entity, AdresseMail) {

        $scope.adresseMail = entity;
        $scope.load = function(id) {
            AdresseMail.get({id : id}, function(result) {
                $scope.adresseMail = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('babylone14166App:adresseMailUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.adresseMail.id != null) {
                AdresseMail.update($scope.adresseMail, onSaveSuccess, onSaveError);
            } else {
                AdresseMail.save($scope.adresseMail, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
