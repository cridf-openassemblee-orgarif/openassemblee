'use strict';

angular.module('openassembleeApp').controller('SeanceDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Seance', 'PresenceElu', 'HemicycleDisposition',
        function ($scope, $stateParams, $modalInstance, entity, Seance, PresenceElu, HemicyclePlan) {

            $scope.seance = entity;
            $scope.seances = [];
            $scope.projetsPlan = [];

            // $scope.presenceelus = PresenceElu.query();
            $scope.load = function (id) {
                Seance.get({id: id}, function (result) {
                    $scope.seance = result;
                });
            };
            Seance.query({sort: 'date,desc'}, function (result, headers) {
                $scope.seances = result;
            });
            HemicyclePlan.query({sort: 'date,desc'}, function (result, headers) {
                $scope.projetsPlan = result;
            });

            var onSaveSuccess = function (result) {
                $scope.$emit('openassembleeApp:seanceUpdate', result);
                $modalInstance.close(result);
                $scope.isSaving = false;
            };

            var onSaveError = function (result) {
                $scope.isSaving = false;
            };

            $scope.save = function () {
                $scope.isSaving = true;
                if ($scope.seance.id != null) {
                    Seance.update($scope.seance, onSaveSuccess, onSaveError);
                } else {
                    Seance.save($scope.seance, onSaveSuccess, onSaveError);
                }
            };

            $scope.clear = function () {
                $modalInstance.dismiss('cancel');
            };
        }]);
