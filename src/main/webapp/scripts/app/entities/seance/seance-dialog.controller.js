"use strict";

angular.module("openassembleeApp").controller("SeanceDialogController", [
    "$scope",
    "$rootScope",
    "$stateParams",
    "$modalInstance",
    "entity",
    "Seance",
    "PresenceElu",
    "HemicyclePlan",
    function (
        $scope,
        $rootScope,
        $stateParams,
        $modalInstance,
        entity,
        Seance,
        PresenceElu,
        HemicyclePlan
    ) {
        $scope.seance = entity;
        $scope.projetsPlan = [];
        $scope.properties = {};

        $scope.load = function (id) {
            Seance.get({ id: id }, function (result) {
                $scope.seance = result;
            });
        };
        HemicyclePlan.query({ sort: "date,desc" }, function (result, headers) {
            $scope.projetsPlan = result;
        });

        var onSaveSuccess = function (result) {
            $scope.$emit("openassembleeApp:seanceUpdate", result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var saveArchive = function (result) {
            if (result.planId) {
                window.saveArchive({
                    planId: result.planId,
                    then: function () {
                        onSaveSuccess(result);
                    },
                });
            } else {
                onSaveSuccess(result);
            }
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            $scope.seance.mandature = { id: $rootScope.currentMandature.id };
            if ($scope.seance.id != null) {
                Seance.update($scope.seance, onSaveSuccess, onSaveError);
            } else {
                var dto = {
                    seance: $scope.seance,
                    seancePlanId: undefined,
                    projetPlanId: undefined,
                };
                if ($scope.properties.planFromProjet) {
                    dto.projetPlanId = $scope.properties.planFromProjet.id;
                }
                Seance.save(dto, saveArchive, onSaveError);
            }
        };

        $scope.clear = function () {
            $modalInstance.dismiss("cancel");
        };
    },
]);
