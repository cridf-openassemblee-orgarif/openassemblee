'use strict';

var tempHours = function (pouvoirCloseOrder) {
    var tempHours = {};
    if (pouvoirCloseOrder.heureFin && pouvoirCloseOrder.heureFin != '') {
        var heureFinAsTime = new Date();
        heureFinAsTime.setHours(+pouvoirCloseOrder.heureFin.substr(0, 2));
        heureFinAsTime.setMinutes(+pouvoirCloseOrder.heureFin.substr(3, 5));
        heureFinAsTime.setSeconds(0);
        heureFinAsTime.setMilliseconds(0);
        tempHours.heureFinAsTime = heureFinAsTime;
    }
    return tempHours;
};

angular.module('openassembleeApp').controller('PouvoirFermerTousDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'Pouvoir',
        function ($scope, $stateParams, $modalInstance, Pouvoir) {

            $scope.pouvoirCloseOrder = {
                seanceId: $stateParams.id
            };
            $scope.pouvoirCloseOrderTemp = {};

            var onSaveSuccess = function (result) {
                $scope.$emit('openassembleeApp:pouvoirUpdate', result);
                $modalInstance.close(result);
                $scope.isSaving = false;
            };

            var onSaveError = function (result) {
                $scope.isSaving = false;
            };

            $scope.finIsNow = function () {
                var date = new Date();
                date.setSeconds(0);
                date.setMilliseconds(0);
                $scope.pouvoirCloseOrder.dateFin = date;
                $scope.pouvoirCloseOrderTemp.heureFinAsTime = date;
            };

            $scope.save = function () {
                $scope.isSaving = true;
                var heureFin = $scope.pouvoirCloseOrderTemp.heureFinAsTime;
                if (heureFin) {
                    var heureFinHours = heureFin.getHours() >= 10 ? heureFin.getHours() : '0' + heureFin.getHours();
                    var heureFinMinutes = heureFin.getMinutes() >= 10 ? heureFin.getMinutes() : '0' + heureFin.getMinutes();
                    $scope.pouvoirCloseOrder.heureFin = heureFinHours + ':' + heureFinMinutes;
                }
                Pouvoir.closeAll($scope.pouvoirCloseOrder, onSaveSuccess, onSaveError);
            };

            $scope.clear = function () {
                $modalInstance.dismiss('cancel');
            };
        }]);
