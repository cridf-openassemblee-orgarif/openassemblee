"use strict";

angular
    .module("openassembleeApp")
    .controller(
        "MandatController",
        function ($scope, $state, $modal, Mandat, MandatSearch) {
            $scope.mandats = [];
            $scope.loadAll = function () {
                Mandat.query(function (result) {
                    $scope.mandats = result;
                });
            };
            $scope.loadAll();

            $scope.search = function () {
                MandatSearch.query(
                    { query: $scope.searchQuery },
                    function (result) {
                        $scope.mandats = result;
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
                $scope.mandat = {
                    dateDebut: null,
                    codeDepartement: null,
                    departement: null,
                    dateDemission: null,
                    motifDemission: null,
                    id: null,
                };
            };
        }
    );
