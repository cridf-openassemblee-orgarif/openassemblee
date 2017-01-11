'use strict';

var tempHours = function (reunionCommissionThematique) {
    var tempHours = {};
    if (reunionCommissionThematique.heureDebut && reunionCommissionThematique.heureDebut !== '') {
        var heureDebutAsTime = new Date();
        heureDebutAsTime.setHours(+reunionCommissionThematique.heureDebut.substr(0, 2));
        heureDebutAsTime.setMinutes(+reunionCommissionThematique.heureDebut.substr(3, 5));
        heureDebutAsTime.setSeconds(0);
        heureDebutAsTime.setMilliseconds(0);
        tempHours.heureDebutAsTime = heureDebutAsTime;
    }
    if (reunionCommissionThematique.heureFin && reunionCommissionThematique.heureFin !== '') {
        var heureFinAsTime = new Date();
        heureFinAsTime.setHours(+reunionCommissionThematique.heureFin.substr(0, 2));
        heureFinAsTime.setMinutes(+reunionCommissionThematique.heureFin.substr(3, 5));
        heureFinAsTime.setSeconds(0);
        heureFinAsTime.setMilliseconds(0);
        tempHours.heureFinAsTime = heureFinAsTime;
    }
    return tempHours;
};

angular.module('babylone14166App').controller('ReunionCommissionThematiqueDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'ReunionCommissionThematique',
        function ($scope, $stateParams, $modalInstance, entity, ReunionCommissionThematique) {

            var initScope = function (entity) {
                $scope.reunionCommissionThematique = entity;
                $scope.reunionTemp = tempHours(entity);
            };

            if (entity.$promise) {
                entity.$promise.then(function callback() {
                    initScope(entity);
                });
            } else {
                initScope(entity);
            }

            $scope.reunionCommissionThematique = entity;
            $scope.load = function (id) {
                ReunionCommissionThematique.get({id: id}, function (result) {
                    initScope(result);
                });
            };

            var onSaveSuccess = function (result) {
                $scope.$emit('babylone14166App:reunionCommissionThematiqueUpdate', result);
                $modalInstance.close(result);
                $scope.isSaving = false;
            };

            var onSaveError = function (result) {
                $scope.isSaving = false;
            };

            $scope.save = function () {
                $scope.isSaving = true;
                var heureDebut = $scope.reunionTemp.heureDebutAsTime;
                if (heureDebut) {
                    var heureDebutHours = heureDebut.getHours() >= 10 ? heureDebut.getHours() : '0' + heureDebut.getHours();
                    var heureDebutMinutes = heureDebut.getMinutes() >= 10 ? heureDebut.getMinutes() : '0' + heureDebut.getMinutes();
                    $scope.reunionCommissionThematique.heureDebut = heureDebutHours + ':' + heureDebutMinutes;
                }
                var heureFin = $scope.reunionTemp.heureFinAsTime;
                if (heureFin) {
                    var heureFinHours = heureFin.getHours() >= 10 ? heureFin.getHours() : '0' + heureFin.getHours();
                    var heureFinMinutes = heureFin.getMinutes() >= 10 ? heureFin.getMinutes() : '0' + heureFin.getMinutes();
                    $scope.reunionCommissionThematique.heureFin = heureFinHours + ':' + heureFinMinutes;
                }
                if ($scope.reunionCommissionThematique.id !== null) {
                    ReunionCommissionThematique.update($scope.reunionCommissionThematique, onSaveSuccess, onSaveError);
                } else {
                    ReunionCommissionThematique.save($scope.reunionCommissionThematique, onSaveSuccess, onSaveError);
                }
            };

            $scope.clear = function () {
                $modalInstance.dismiss('cancel');
            };
        }]);
