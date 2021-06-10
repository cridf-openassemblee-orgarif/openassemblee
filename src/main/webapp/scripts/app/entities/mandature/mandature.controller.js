"use strict";

angular
    .module("openassembleeApp")
    .controller(
        "MandatureController",
        function ($scope, $state, $modal, Mandature, MandatureSearch) {
            $scope.mandatures = [];
            $scope.loadAll = function () {
                Mandature.query(function (result) {
                    $scope.mandatures = result;
                });
            };
            $scope.loadAll();

            $scope.search = function () {
                MandatureSearch.query(
                    { query: $scope.searchQuery },
                    function (result) {
                        $scope.mandatures = result;
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
                $scope.mandature = {
                    anneeDebut: null,
                    anneeFin: null,
                    dateDebut: null,
                    current: null,
                    id: null,
                };
            };
        }
    );
