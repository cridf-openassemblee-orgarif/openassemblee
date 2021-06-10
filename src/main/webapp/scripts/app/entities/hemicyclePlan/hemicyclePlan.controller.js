"use strict";

angular
    .module("openassembleeApp")
    .controller(
        "HemicyclePlanController",
        function (
            $scope,
            $state,
            $modal,
            HemicyclePlan,
            HemicyclePlanSearch,
            $http
        ) {
            $scope.hemicyclePlans = [];
            $http({
                method: "GET",
                url: "api/hemicyclePlans-projets",
            }).then(
                function successCallback(result) {
                    $scope.hemicyclePlans = result.data;
                },
                function errorCallback(response) {}
            );

            $scope.search = function () {
                HemicyclePlanSearch.query(
                    { query: $scope.searchQuery },
                    function (result) {
                        $scope.hemicyclePlans = result;
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
                $scope.hemicyclePlan = {
                    label: null,
                    jsonPlan: null,
                    creationDate: null,
                    lastModificationDate: null,
                    id: null,
                };
            };
        }
    );
