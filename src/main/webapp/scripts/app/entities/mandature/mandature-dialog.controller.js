"use strict";

angular.module("openassembleeApp").controller("MandatureDialogController", [
    "$scope",
    "$stateParams",
    "$modalInstance",
    "entity",
    "Mandature",
    function ($scope, $stateParams, $modalInstance, entity, Mandature) {
        $scope.mandature = entity;
        $scope.load = function (id) {
            Mandature.get({ id: id }, function (result) {
                $scope.mandature = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit("openassembleeApp:mandatureUpdate", result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.mandature.id != null) {
                Mandature.update($scope.mandature, onSaveSuccess, onSaveError);
            } else {
                Mandature.save($scope.mandature, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function () {
            $modalInstance.dismiss("cancel");
        };
    },
]);
