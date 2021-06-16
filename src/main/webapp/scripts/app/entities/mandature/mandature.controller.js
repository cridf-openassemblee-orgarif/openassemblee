"use strict";

angular
    .module("openassembleeApp")
    .controller(
        "MandatureController",
        function (
            $scope,
            $rootScope,
            $state,
            $modal,
            Mandature,
            MandatureSearch,
            $http,
            Principal
        ) {
            $scope.mandatures = [];
            $scope.mandatureCouranteForAll = {};
            $scope.loadAll = function () {
                Mandature.query(function (result) {
                    $scope.mandatures = result;
                    $scope.mandatureCouranteForAll = result.filter(function (
                        m
                    ) {
                        return m.current;
                    })[0];
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

            $scope.setMandatureForSession = function (mandature) {
                $http
                    .post(
                        "api/mandatures/set-current-for-session/" + mandature.id
                    )
                    .success(function () {
                        Principal.identity(true);
                    });
            };
        }
    );
