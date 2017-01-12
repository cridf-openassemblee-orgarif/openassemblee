'use strict';

angular.module('babylone14166App').controller('AppartenanceGroupePolitiqueDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'AppartenanceGroupePolitique', 'Elu', 'GroupePolitique',
        function ($scope, $stateParams, $modalInstance, entity, AppartenanceGroupePolitique, Elu, GroupePolitique) {

            $scope.appartenanceGroupePolitique = entity;
            $scope.elus = Elu.query();
            $scope.groupepolitiques = GroupePolitique.query();
            $scope.load = function (id) {
                AppartenanceGroupePolitique.get({id: id}, function (result) {
                    $scope.appartenanceGroupePolitique = result;
                });
            };

            var onSaveSuccess = function (result) {
                $scope.$emit('babylone14166App:appartenanceGroupePolitiqueUpdate', result);
                $modalInstance.close(result);
                $scope.isSaving = false;
            };

            var onSaveError = function (result) {
                $scope.isSaving = false;
            };

            $scope.save = function () {
                $scope.isSaving = true;
                $scope.appartenanceGroupePolitique.elu = {id: $stateParams.id};
                if ($scope.appartenanceGroupePolitique.id != null) {
                    AppartenanceGroupePolitique.update($scope.appartenanceGroupePolitique, onSaveSuccess, onSaveError);
                } else {
                    AppartenanceGroupePolitique.save($scope.appartenanceGroupePolitique, onSaveSuccess, onSaveError);
                }
            };

            $scope.clear = function () {
                $modalInstance.dismiss('cancel');
            };
        }]);
