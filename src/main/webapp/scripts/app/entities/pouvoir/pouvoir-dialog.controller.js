'use strict';

var tempHours = function (pouvoir) {
    var tempHours = {};
    if(pouvoir.heureDebut && pouvoir.heureDebut != '') {
        var heureDebutAsTime = new Date();
        heureDebutAsTime.setHours(+pouvoir.heureDebut.substr(0, 2));
        heureDebutAsTime.setMinutes(+pouvoir.heureDebut.substr(3, 5));
        heureDebutAsTime.setSeconds(0);
        heureDebutAsTime.setMilliseconds(0);
        tempHours.heureDebutAsTime = heureDebutAsTime;
    }
    if(pouvoir.heureFin && pouvoir.heureFin != '') {
        var heureFinAsTime = new Date();
        heureFinAsTime.setHours(+pouvoir.heureFin.substr(0, 2));
        heureFinAsTime.setMinutes(+pouvoir.heureFin.substr(3, 5));
        heureFinAsTime.setSeconds(0);
        heureFinAsTime.setMilliseconds(0);
        tempHours.heureFinAsTime = heureFinAsTime;
    }
    return tempHours;
}

angular.module('babylone14166App').controller('PouvoirDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Pouvoir', 'Elu',
        function($scope, $stateParams, $modalInstance, entity, Pouvoir, Elu) {

        $scope.pouvoir = entity;
        if(entity.$promise) {
            entity.$promise.then(function callback() {
                $scope.pouvoirTemp = tempHours($scope.pouvoir);
            });
        } else {
            $scope.pouvoirTemp = tempHours(entity);
        }
        Elu.query(function(elus) {
            $scope.elus = elus.map(function (e) { return e.elu });
        });
        $scope.load = function(id) {
            Pouvoir.get({id : id}, function(result) {
                $scope.pouvoir = result;
                $scope.pouvoirTemp = tempHours(result);
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

        $scope.debutIsNow = function() {
            var date = new Date();
            date.setSeconds(0);
            date.setMilliseconds(0);
            $scope.pouvoir.dateDebut = date;
            $scope.pouvoirTemp.heureDebutAsTime = date;
        };

        $scope.finIsNow = function() {
            var date = new Date();
            date.setSeconds(0);
            date.setMilliseconds(0);
            $scope.pouvoir.dateFin = date;
            $scope.pouvoirTemp.heureFinAsTime = date;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            var heureDebut = $scope.pouvoirTemp.heureDebutAsTime;
            if(heureDebut) {
                var heureDebutHours = heureDebut.getHours() > 10 ? heureDebut.getHours() : '0' + heureDebut.getHours();
                var heureDebutMinutes = heureDebut.getMinutes() > 10 ? heureDebut.getMinutes() : '0' + heureDebut.getMinutes();
                $scope.pouvoir.heureDebut = heureDebutHours + ':' + heureDebutMinutes;
            }
            var heureFin = $scope.pouvoirTemp.heureFinAsTime;
            if(heureFin) {
                var heureFinHours = heureFin.getHours() > 10 ? heureFin.getHours() : '0' + heureFin.getHours();
                var heureFinMinutes = heureFin.getMinutes() > 10 ? heureFin.getMinutes() : '0' + heureFin.getMinutes();
                $scope.pouvoir.heureFin = heureFinHours + ':' + heureFinMinutes;
            }
            if ($scope.pouvoir.id != null) {
                Pouvoir.update($scope.pouvoir, onSaveSuccess, onSaveError);
            } else {
                Pouvoir.save($scope.pouvoir, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
