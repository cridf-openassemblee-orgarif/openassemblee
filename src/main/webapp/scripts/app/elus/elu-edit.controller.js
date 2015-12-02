'use strict';

angular.module('babylone14166App').controller('EluEditController',
    ['$scope', '$stateParams', 'entity', 'Elu', 'AdressePostale',
        function ($scope, $stateParams, entity, Elu, AdressePostale) {

            $scope.elu = entity;
            $scope.adressepostales = AdressePostale.query();
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
        }]);
