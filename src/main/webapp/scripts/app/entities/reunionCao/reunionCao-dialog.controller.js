'use strict';

var tempHours = function (reunion) {
    var tempHours = {};
    if (reunion.heureDebut && reunion.heureDebut !== '') {
        var heureDebutAsTime = new Date();
        heureDebutAsTime.setHours(+reunion.heureDebut.substr(0, 2));
        heureDebutAsTime.setMinutes(+reunion.heureDebut.substr(3, 5));
        heureDebutAsTime.setSeconds(0);
        heureDebutAsTime.setMilliseconds(0);
        tempHours.heureDebutAsTime = heureDebutAsTime;
    }
    if (reunion.heureFin && reunion.heureFin !== '') {
        var heureFinAsTime = new Date();
        heureFinAsTime.setHours(+reunion.heureFin.substr(0, 2));
        heureFinAsTime.setMinutes(+reunion.heureFin.substr(3, 5));
        heureFinAsTime.setSeconds(0);
        heureFinAsTime.setMilliseconds(0);
        tempHours.heureFinAsTime = heureFinAsTime;
    }
    return tempHours;
};

angular.module('openassembleeApp').controller('ReunionCaoDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'ReunionCao', 'Elu',
        function ($scope, $stateParams, $modalInstance, entity, ReunionCao, Elu) {

            $scope.temp = {
                selectedElus: []
            };
            Elu.query(function (elus) {
                $scope.elus = elus.map(function (dto) {
                    return {
                        id: dto.elu.id,
                        nom: dto.elu.nom,
                        prenom: dto.elu.prenom,
                        groupePolitique: dto.groupePolitique !== null ? dto.groupePolitique.nomCourt : 'sans groupe'
                    };
                });
            });

            var initScope = function (entity) {
                $scope.reunion = entity;
                $scope.reunionTemp = tempHours(entity);
            };

            if (entity.$promise) {
                entity.$promise.then(function callback() {
                    initScope(entity);
                });
            } else {
                initScope(entity);
            }

            $scope.reunion = entity;
            $scope.load = function (id) {
                ReunionCao.get({id: id}, function (result) {
                    initScope(result);
                });
            };

            var onSaveSuccess = function (result) {
                $scope.$emit('openassembleeApp:reunionCaoUpdate', result);
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
                    $scope.reunion.heureDebut = heureDebutHours + ':' + heureDebutMinutes;
                }
                var heureFin = $scope.reunionTemp.heureFinAsTime;
                if (heureFin) {
                    var heureFinHours = heureFin.getHours() >= 10 ? heureFin.getHours() : '0' + heureFin.getHours();
                    var heureFinMinutes = heureFin.getMinutes() >= 10 ? heureFin.getMinutes() : '0' + heureFin.getMinutes();
                    $scope.reunion.heureFin = heureFinHours + ':' + heureFinMinutes;
                }
                $scope.reunion.presenceElus = $scope.temp.selectedElus.map(function (e, i) {
                    return {
                        // id sont settées car sinon le Set Java ne garde qu'une instance
                        id: i,
                        elu: {id: e.id}
                    }
                });
                if ($scope.reunion.id !== null) {
                    ReunionCao.update($scope.reunion, onSaveSuccess, onSaveError);
                } else {
                    ReunionCao.save($scope.reunion, onSaveSuccess, onSaveError);
                }
            };

            $scope.clear = function () {
                $modalInstance.dismiss('cancel');
            };
        }]);
