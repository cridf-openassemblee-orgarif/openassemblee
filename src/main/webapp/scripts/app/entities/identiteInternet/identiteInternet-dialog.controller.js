"use strict";

angular
    .module("openassembleeApp")
    .controller("IdentiteInternetDialogController", [
        "$scope",
        "$stateParams",
        "$modalInstance",
        "entity",
        "IdentiteInternet",
        "Elu",
        function (
            $scope,
            $stateParams,
            $modalInstance,
            entity,
            IdentiteInternet,
            Elu
        ) {
            $scope.identiteInternet = entity;
            $scope.load = function (id) {
                IdentiteInternet.get({ id: id }, function (result) {
                    $scope.identiteInternet = result;
                });
            };

            var onSaveSuccess = function (result) {
                $scope.$emit("openassembleeApp:identiteInternetUpdate", result);
                $modalInstance.close(result);
                $scope.isSaving = false;
            };

            var onSaveError = function (result) {
                $scope.isSaving = false;
            };

            $scope.save = function () {
                $scope.isSaving = true;
                if ($scope.identiteInternet.id != null) {
                    Elu.updateIdentiteInternet(
                        { id: $stateParams.id },
                        $scope.identiteInternet,
                        onSaveSuccess,
                        onSaveError
                    );
                } else {
                    Elu.saveIdentiteInternet(
                        { id: $stateParams.id },
                        $scope.identiteInternet,
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
