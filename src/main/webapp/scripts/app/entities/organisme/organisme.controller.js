"use strict";

angular
    .module("openassembleeApp")
    .controller(
        "OrganismeController",
        function (
            $scope,
            $state,
            $modal,
            Organisme,
            OrganismeSearch,
            ParseLinks
        ) {
            $scope.organismes = [];
            $scope.page = 0;
            $scope.loadAll = function () {
                Organisme.query(
                    { page: $scope.page, size: 10 },
                    function (result, headers) {
                        $scope.links = ParseLinks.parse(headers("link"));
                        $scope.organismes = result;
                    }
                );
            };
            $scope.loadPage = function (page) {
                $scope.page = page;
                $scope.loadAll();
            };
            $scope.loadAll();

            $scope.search = function () {
                OrganismeSearch.query(
                    { query: $scope.searchQuery },
                    function (result) {
                        $scope.organismes = result;
                    },
                    function (response) {
                        if (response.status === 404) {
                            $scope.loadAll();
                        }
                    }
                );
            };

            $scope.refresh = function () {
                $scope.loadAll();
                $scope.clear();
            };

            $scope.clear = function () {
                $scope.organisme = {
                    nom: null,
                    codeRNE: null,
                    sigle: null,
                    type: null,
                    secteur: null,
                    dateDebut: null,
                    dateFin: null,
                    motifFin: null,
                    id: null,
                };
            };
        }
    );
