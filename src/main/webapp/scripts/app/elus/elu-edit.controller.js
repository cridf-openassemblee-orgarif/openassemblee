'use strict';

angular.module('babylone14166App').controller('EluEditController',
    ['$scope', '$stateParams', 'entity', 'Elu',
        function ($scope, $stateParams, entity, Elu) {

            $scope.elu = entity;
            $scope.load = function (id) {
                Elu.get({id: id}, function (result) {
                    $scope.elu = result;
                });
            };

            var onSaveSuccess = function (result) {
                $scope.$emit('babylone14166App:eluUpdate', result);
                $scope.isSaving = false;
            };

            var onSaveError = function (result) {
                $scope.isSaving = false;
            };

            $scope.save = function () {
                $scope.isSaving = true;
                if ($scope.elu.id != null) {
                    Elu.update($scope.elu, onSaveSuccess, onSaveError);
                } else {
                    Elu.save($scope.elu, onSaveSuccess, onSaveError);
                }
            };

            $scope.clear = function () {
                // TODO
            };

            $scope.addAdressePostale = function () {
                if ($scope.elu.adressesPostales == null) {
                    $scope.elu.adressesPostales = [];
                }
                $scope.elu.adressesPostales.push({});
            };

            $scope.addNumeroTelephone = function () {
                if ($scope.elu.numerosTelephones == null) {
                    $scope.elu.numerosTelephones = [];
                }
                $scope.elu.numerosTelephones.push({});
            };
        }]);
