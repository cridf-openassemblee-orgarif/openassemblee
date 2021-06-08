'use strict';

angular.module('openassembleeApp').controller('ListeElectoraleDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'ListeElectorale', 'Mandature',
        function($scope, $stateParams, $modalInstance, entity, ListeElectorale, Mandature) {

        $scope.listeElectorale = entity;
        $scope.mandatures = Mandature.query();
        $scope.load = function(id) {
            ListeElectorale.get({id : id}, function(result) {
                $scope.listeElectorale = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('openassembleeApp:listeElectoraleUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.listeElectorale.id != null) {
                ListeElectorale.update($scope.listeElectorale, onSaveSuccess, onSaveError);
            } else {
                ListeElectorale.save($scope.listeElectorale, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
