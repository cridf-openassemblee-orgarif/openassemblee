"use strict";

angular
    .module("openassembleeApp")
    .controller("GroupePolitiqueDialogController", [
        "$scope",
        "$rootScope",
        "$stateParams",
        "$modalInstance",
        "entity",
        "GroupePolitique",
        function (
            $scope,
            $rootScope,
            $stateParams,
            $modalInstance,
            entity,
            GroupePolitique
        ) {
            $scope.groupePolitique = entity;
            $scope.load = function (id) {
                GroupePolitique.get({ id: id }, function (result) {
                    $scope.groupePolitique = result;
                });
            };

            var onSaveSuccess = function (result) {
                $scope.$emit("openassembleeApp:groupePolitiqueUpdate", result);
                $modalInstance.close(result);
                $scope.isSaving = false;
            };

            var onSaveError = function (result) {
                $scope.isSaving = false;
            };

            $scope.save = function () {
                $scope.isSaving = true;
                $scope.groupePolitique.mandature = {
                    id: $rootScope.currentMandature.id,
                };
                if ($scope.groupePolitique.id != null) {
                    GroupePolitique.update(
                        $scope.groupePolitique,
                        onSaveSuccess,
                        onSaveError
                    );
                } else {
                    GroupePolitique.save(
                        $scope.groupePolitique,
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
