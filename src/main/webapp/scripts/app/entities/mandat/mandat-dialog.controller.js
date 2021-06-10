"use strict";

angular.module("openassembleeApp").controller("MandatDialogController", [
    "$scope",
    "$stateParams",
    "$modalInstance",
    "entity",
    "Mandat",
    "Elu",
    "Mandature",
    "ListeElectorale",
    function (
        $scope,
        $stateParams,
        $modalInstance,
        entity,
        Mandat,
        Elu,
        Mandature,
        ListeElectorale
    ) {
        $scope.mandat = entity;
        $scope.elus = Elu.query();
        $scope.mandatures = Mandature.query();
        $scope.listeelectorales = ListeElectorale.query();
        $scope.load = function (id) {
            Mandat.get({ id: id }, function (result) {
                $scope.mandat = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit("openassembleeApp:mandatUpdate", result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.mandat.id != null) {
                Mandat.update($scope.mandat, onSaveSuccess, onSaveError);
            } else {
                Mandat.save($scope.mandat, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function () {
            $modalInstance.dismiss("cancel");
        };
    },
]);
