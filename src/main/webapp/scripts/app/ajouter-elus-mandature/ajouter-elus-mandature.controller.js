"use strict";

var csvHeader =
    "civilité;nom;prénom;date de naissance;liste candidats;liste candidats court;département;code département\n";

angular
    .module("openassembleeApp")
    .controller("AjouterElusMandature", function ($scope, $http, $stateParams) {
        $scope.csvData = csvHeader;
        $scope.candidatCorrespondanceList = [];
        $scope.props = {
            hasErrors: false,
        };

        $scope.submitCsv = function () {
            var csvData = $scope.csvData.replace(csvHeader, "");
            $scope.props.hasErrors = false;
            $http
                .post("/api/ajouter-elus-mandature/prepareImport", csvData)
                .then(function (response) {
                    $scope.csvData = csvHeader;
                    $scope.candidatCorrespondanceList = response.data;
                    angular.forEach(response.data, function (cc) {
                        if (cc.errors.length !== 0) {
                            $scope.props.hasErrors = true;
                        }
                    });
                });
        };

        $scope.submitCandidats = function () {
            $http
                .post(
                    "/api/ajouter-elus-mandature/ajouter/" + $stateParams.id,
                    $scope.candidatCorrespondanceList
                )
                .then(function (response) {
                    $scope.candidatCorrespondanceList = [];
                });
        };

        $scope.deleteCorrespondance = function (cc) {
            cc.elu = null;
        };
    });
