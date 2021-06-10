"use strict";

angular
    .module("openassembleeApp")
    .controller(
        "HemicycleConfigurationController",
        function (
            $scope,
            $state,
            $modal,
            HemicycleConfiguration,
            HemicycleConfigurationSearch
        ) {
            $scope.hemicycleConfigurations = [];
            $scope.loadAll = function () {
                HemicycleConfiguration.query(function (result) {
                    $scope.hemicycleConfigurations = result;
                });
            };
            $scope.loadAll();

            $scope.search = function () {
                HemicycleConfigurationSearch.query(
                    { query: $scope.searchQuery },
                    function (result) {
                        $scope.hemicycleConfigurations = result;
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
                $scope.hemicycleConfiguration = {
                    label: null,
                    jsonConfiguration: null,
                    creationDate: null,
                    lastModificationDate: null,
                    frozen: null,
                    frozenDate: null,
                    id: null,
                };
            };
        }
    );
