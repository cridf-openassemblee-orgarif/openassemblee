'use strict';

var tempHours = function (pouvoir) {
    var tempHours = {};
    if (pouvoir.heureDebut && pouvoir.heureDebut != '') {
        var heureDebutAsTime = new Date();
        heureDebutAsTime.setHours(+pouvoir.heureDebut.substr(0, 2));
        heureDebutAsTime.setMinutes(+pouvoir.heureDebut.substr(3, 5));
        heureDebutAsTime.setSeconds(0);
        heureDebutAsTime.setMilliseconds(0);
        tempHours.heureDebutAsTime = heureDebutAsTime;
    }
    if (pouvoir.heureFin && pouvoir.heureFin != '') {
        var heureFinAsTime = new Date();
        heureFinAsTime.setHours(+pouvoir.heureFin.substr(0, 2));
        heureFinAsTime.setMinutes(+pouvoir.heureFin.substr(3, 5));
        heureFinAsTime.setSeconds(0);
        heureFinAsTime.setMilliseconds(0);
        tempHours.heureFinAsTime = heureFinAsTime;
    }
    return tempHours;
};

angular.module('babylone14166App').controller('PouvoirDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Pouvoir', 'Elu',
        function ($scope, $stateParams, $modalInstance, entity, Pouvoir, Elu) {

            var initPouvoirScope = function (entity) {
                $scope.pouvoir = entity;
                $scope.pouvoirTemp = tempHours(entity);
            };

            if (entity.$promise) {
                entity.$promise.then(function callback() {
                    initPouvoirScope(entity);
                });
            } else {
                initPouvoirScope(entity);
            }

            $scope.autoclosePrecedentPouvoir = false;
            $scope.pouvoirsDejaExistant = function () {
                return $scope.openPouvoirs.filter(function (pv) {
                    return $scope.pouvoir.eluCedeur != null &&
                        ((pv.eluCedeur != null
                        && pv.eluCedeur.id == $scope.pouvoir.eluCedeur.id)
                        || (pv.eluBeneficiaire != null
                        && pv.eluBeneficiaire.id == $scope.pouvoir.eluCedeur.id))
                });
            };

            $scope.openPouvoirs = [];
            Pouvoir.getAllOpen(function (result) {
                $scope.openPouvoirs = result;
            });

            Elu.query(function (elus) {
                $scope.elus = elus.map(function (e) {
                    var gp = e.groupePolitique != null ? e.groupePolitique.nomCourt : 'sans groupe';
                    e.elu.groupePolitique = gp;
                    return e.elu;
                });
            });
            $scope.load = function (id) {
                Pouvoir.get({id: id}, function (result) {
                    initPouvoirScope(result)
                });
            };

            var onSaveSuccess = function (result) {
                $scope.$emit('babylone14166App:pouvoirUpdate', result);
                $modalInstance.close(result);
                $scope.isSaving = false;
            };

            var onSaveError = function (result) {
                $scope.isSaving = false;
            };

            $scope.debutIsNow = function () {
                var date = new Date();
                date.setSeconds(0);
                date.setMilliseconds(0);
                $scope.pouvoir.dateDebut = date;
                $scope.pouvoirTemp.heureDebutAsTime = date;
            };

            $scope.finIsNow = function () {
                var date = new Date();
                date.setSeconds(0);
                date.setMilliseconds(0);
                $scope.pouvoir.dateFin = date;
                $scope.pouvoirTemp.heureFinAsTime = date;
            };

            var savePouvoir = function () {
                if ($scope.pouvoir.id != null) {
                    Pouvoir.update($scope.pouvoir, onSaveSuccess, onSaveError);
                } else {
                    Pouvoir.save($scope.pouvoir, onSaveSuccess, onSaveError);
                }
            };
            $scope.save = function () {
                $scope.isSaving = true;
                var heureDebut = $scope.pouvoirTemp.heureDebutAsTime;
                if (heureDebut) {
                    var heureDebutHours = heureDebut.getHours() >= 10 ? heureDebut.getHours() : '0' + heureDebut.getHours();
                    var heureDebutMinutes = heureDebut.getMinutes() >= 10 ? heureDebut.getMinutes() : '0' + heureDebut.getMinutes();
                    $scope.pouvoir.heureDebut = heureDebutHours + ':' + heureDebutMinutes;
                }
                var heureFin = $scope.pouvoirTemp.heureFinAsTime;
                if (heureFin) {
                    var heureFinHours = heureFin.getHours() >= 10 ? heureFin.getHours() : '0' + heureFin.getHours();
                    var heureFinMinutes = heureFin.getMinutes() >= 10 ? heureFin.getMinutes() : '0' + heureFin.getMinutes();
                    $scope.pouvoir.heureFin = heureFinHours + ':' + heureFinMinutes;
                }
                var pouvoirsDejaExistant = $scope.pouvoirsDejaExistant();
                if (pouvoirsDejaExistant.length > 0 && $scope.autoclosePrecedentPouvoir) {
                    // Ne propose pas la fermeture si plus d'un pouvoir
                    var pouvoirDejaExistant = pouvoirsDejaExistant[0];
                    // TODO se plante s'il n'y en a pas...
                    pouvoirDejaExistant.dateFin = $scope.pouvoir.dateDebut;
                    pouvoirDejaExistant.heureFin = $scope.pouvoir.heureDebut;
                    Pouvoir.update(pouvoirDejaExistant, function () {
                        savePouvoir()
                    }, onSaveError);
                } else {
                    savePouvoir()
                }
            };

            $scope.clear = function () {
                $modalInstance.dismiss('cancel');
            };
        }]);
