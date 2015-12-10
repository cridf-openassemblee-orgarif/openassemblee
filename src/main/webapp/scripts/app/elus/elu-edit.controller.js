'use strict';

angular.module('babylone14166App').controller('EluEditController',
    ['$scope', '$state', '$stateParams', 'entity', 'Elu',
        function ($scope, $state, $stateParams, entity, Elu) {

            $scope.elu = entity;
            $scope.load = function (id) {
                Elu.get({id: id}, function (result) {
                    $scope.elu = result;
                });
            };

            var onSaveSuccess = function (result) {
                $scope.$emit('babylone14166App:eluUpdate', result);
                $scope.isSaving = false;
                $state.go('elus.detail', {id: result.id});
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

            $scope.add = function (obj) {
                if ($scope.elu[obj] == null) {
                    $scope.elu[obj] = [];
                }
                $scope.elu[obj].push({});
            }

            $scope.remove = function (obj, index) {
                $scope.elu[obj].splice(index, 1);
            }
        }]);
