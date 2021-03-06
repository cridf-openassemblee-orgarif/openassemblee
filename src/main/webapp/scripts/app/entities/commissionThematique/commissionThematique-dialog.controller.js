"use strict";

angular
    .module("openassembleeApp")
    .controller("CommissionThematiqueDialogController", [
        "$scope",
        "$rootScope",
        "$stateParams",
        "$modalInstance",
        "entity",
        "CommissionThematique",
        function (
            $scope,
            $rootScope,
            $stateParams,
            $modalInstance,
            entity,
            CommissionThematique
        ) {
            $scope.dto = entity;
            $scope.load = function (id) {
                CommissionThematique.get({ id: id }, function (result) {
                    $scope.dto = result;
                });
            };

            var onSaveSuccess = function (result) {
                $scope.$emit(
                    "openassembleeApp:commissionThematiqueUpdate",
                    result
                );
                $modalInstance.close(result);
                $scope.isSaving = false;
            };

            var onSaveError = function (result) {
                $scope.isSaving = false;
            };

            $scope.save = function () {
                $scope.isSaving = true;
                $scope.dto.commissionThematique.mandature = {
                    id: $rootScope.currentMandature.id,
                };
                if ($scope.dto.commissionThematique.id != null) {
                    CommissionThematique.update(
                        $scope.dto.commissionThematique,
                        onSaveSuccess,
                        onSaveError
                    );
                } else {
                    CommissionThematique.save(
                        $scope.dto.commissionThematique,
                        onSaveSuccess,
                        onSaveError
                    );
                }
            };

            $scope.clear = function () {
                $modalInstance.dismiss("cancel");
            };
        },
    ]);
