"use strict";

angular
    .module("openassembleeApp")
    .controller("DistinctionHonorifiqueDialogController", [
        "$scope",
        "$stateParams",
        "$modalInstance",
        "entity",
        "DistinctionHonorifique",
        "Elu",
        function (
            $scope,
            $stateParams,
            $modalInstance,
            entity,
            DistinctionHonorifique,
            Elu
        ) {
            $scope.distinctionHonorifique = entity;
            $scope.elus = Elu.query();
            $scope.load = function (id) {
                DistinctionHonorifique.get({ id: id }, function (result) {
                    $scope.distinctionHonorifique = result;
                });
            };

            var onSaveSuccess = function (result) {
                $scope.$emit(
                    "openassembleeApp:distinctionHonorifiqueUpdate",
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
                $scope.distinctionHonorifique.elu = { id: $stateParams.id };
                if ($scope.distinctionHonorifique.id != null) {
                    DistinctionHonorifique.update(
                        $scope.distinctionHonorifique,
                        onSaveSuccess,
                        onSaveError
                    );
                } else {
                    DistinctionHonorifique.save(
                        $scope.distinctionHonorifique,
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
