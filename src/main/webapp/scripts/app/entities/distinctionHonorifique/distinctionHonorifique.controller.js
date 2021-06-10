"use strict";

angular
    .module("openassembleeApp")
    .controller(
        "DistinctionHonorifiqueController",
        function (
            $scope,
            $state,
            $modal,
            DistinctionHonorifique,
            DistinctionHonorifiqueSearch
        ) {
            $scope.distinctionHonorifiques = [];
            $scope.loadAll = function () {
                DistinctionHonorifique.query(function (result) {
                    $scope.distinctionHonorifiques = result;
                });
            };
            $scope.loadAll();

            $scope.search = function () {
                DistinctionHonorifiqueSearch.query(
                    { query: $scope.searchQuery },
                    function (result) {
                        $scope.distinctionHonorifiques = result;
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
                $scope.distinctionHonorifique = {
                    titre: null,
                    date: null,
                    id: null,
                };
            };
        }
    );
