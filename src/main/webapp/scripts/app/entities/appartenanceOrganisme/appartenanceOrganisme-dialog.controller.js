"use strict";

angular
    .module("openassembleeApp")
    .controller("AppartenanceOrganismeDialogController", [
        "$scope",
        "$stateParams",
        "$modalInstance",
        "entity",
        "AppartenanceOrganisme",
        "Elu",
        function (
            $scope,
            $stateParams,
            $modalInstance,
            entity,
            AppartenanceOrganisme,
            Elu
        ) {
            $scope.appartenanceOrganisme = entity;

            var onSaveSuccess = function (result) {
                $scope.$emit(
                    "openassembleeApp:appartenanceOrganismeUpdate",
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
                $scope.appartenanceOrganisme.elu = { id: $stateParams.id };
                if (typeof $scope.organisme === "object") {
                    $scope.appartenanceOrganisme.organisme =
                        $scope.organisme.nom;
                    $scope.appartenanceOrganisme.codeRNE =
                        $scope.organisme.codeRNE;
                } else if (typeof $scope.organisme === "string") {
                    $scope.appartenanceOrganisme.organisme = $scope.organisme;
                } // else NE PAS TOUCHER sinon organisme est supprimé dans l'appartenance de départ
                // lorsqu'on édite une fin d'appartenance ($scope.organisme est null)
                if ($scope.appartenanceOrganisme.id != null) {
                    AppartenanceOrganisme.update(
                        $scope.appartenanceOrganisme,
                        onSaveSuccess,
                        onSaveError
                    );
                } else {
                    AppartenanceOrganisme.save(
                        $scope.appartenanceOrganisme,
                        onSaveSuccess,
                        onSaveError
                    );
                }
            };

            $scope.clear = function () {
                $modalInstance.dismiss("cancel");
            };

            $scope.organismesAutocomplete = {
                limit: 10,
                display: "nom",
                source: new Bloodhound({
                    datumTokenizer: Bloodhound.tokenizers.whitespace,
                    queryTokenizer: Bloodhound.tokenizers.whitespace,
                    remote: {
                        url: "api/_search/organismes/%QUERY",
                        wildcard: "%QUERY",
                    },
                }),
                templates: {
                    suggestion: function (data) {
                        return (
                            "<p><b>" +
                            data.codeRNE +
                            "</b> - " +
                            data.nom +
                            "</p>"
                        );
                    },
                },
            };
        },
    ]);
