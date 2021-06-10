"use strict";

angular
    .module("openassembleeApp")
    .controller(
        "PouvoirController",
        function ($scope, $state, $modal, Pouvoir, PouvoirSearch, ParseLinks) {
            $scope.pouvoirs = [];
            $scope.page = 0;
            $scope.loadAll = function () {
                Pouvoir.query(
                    { page: $scope.page, size: 20, sort: "dateDebut,desc" },
                    function (result, headers) {
                        $scope.links = ParseLinks.parse(headers("link"));
                        $scope.pouvoirs = result;
                    }
                );
            };
            $scope.loadPage = function (page) {
                $scope.page = page;
                $scope.loadAll();
            };
            $scope.loadAll();

            $scope.search = function () {
                PouvoirSearch.query(
                    { query: $scope.searchQuery },
                    function (result) {
                        $scope.pouvoirs = result;
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
                $scope.pouvoir = {
                    dateDebut: null,
                    heureDebut: null,
                    dateFin: null,
                    heureFin: null,
                    id: null,
                };
            };
        }
    );
