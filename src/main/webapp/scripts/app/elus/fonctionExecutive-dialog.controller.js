'use strict';

angular.module('babylone14166App').controller('FonctionExecutiveDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'FonctionExecutive', 'Elu',
        function ($scope, $stateParams, $modalInstance, entity, FonctionExecutive, Elu) {

            $scope.fonctionExecutive = entity;
            $scope.elus = Elu.query();
            $scope.load = function (id) {
                FonctionExecutive.get({id: id}, function (result) {
                    $scope.fonctionExecutive = result;
                });
            };

            var onSaveSuccess = function (result) {
                $scope.$emit('babylone14166App:fonctionExecutiveUpdate', result);
                $modalInstance.close(result);
                $scope.isSaving = false;
            };

            var onSaveError = function (result) {
                $scope.isSaving = false;
            };

            $scope.save = function () {
                $scope.isSaving = true;
                $scope.fonctionExecutive.elu = $stateParams.id;
                if ($scope.fonctionExecutive.id != null) {
                    FonctionExecutive.update($scope.fonctionExecutive, onSaveSuccess, onSaveError);
                } else {
                    FonctionExecutive.save($scope.fonctionExecutive, onSaveSuccess, onSaveError);
                }
            };

            $scope.clear = function () {
                $modalInstance.dismiss('cancel');
            };
        }]);
