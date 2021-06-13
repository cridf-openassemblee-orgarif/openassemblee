"use strict";

angular.module("openassembleeApp").controller("MandatDialogController", [
    "$scope",
    "$rootScope",
    "$stateParams",
    "$modalInstance",
    "entity",
    "Mandat",
    "Elu",
    "Mandature",
    "ListeElectorale",
    function (
        $scope,
        $rootScope,
        $stateParams,
        $modalInstance,
        entity,
        Mandat,
        Elu,
        Mandature,
        ListeElectorale
    ) {
        $scope.mandat = entity;
        $scope.dto = {
            demissionDiffusion: false,
        };
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
            $scope.mandat.elu = {
                id: $stateParams.id,
            };
            $scope.mandat.mandature = {
                id: $rootScope.currentMandature.id,
            };
            var dto = {
                mandat: $scope.mandat,
                demissionDiffusion: $scope.dto.demissionDiffusion,
            };
            if ($scope.mandat.id != null) {
                Mandat.update(dto, onSaveSuccess, onSaveError);
            } else {
                Mandat.save(dto, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function () {
            $modalInstance.dismiss("cancel");
        };
    },
]);
