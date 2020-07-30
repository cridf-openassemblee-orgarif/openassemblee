'use strict';

angular.module('openassembleeApp')
    .controller('HemicyclePlanController', function ($scope, $state, $modal, HemicyclePlan, HemicyclePlanSearch) {
      
        $scope.hemicyclePlans = [];
        $scope.loadAll = function() {
            HemicyclePlan.query(function(result) {
               $scope.hemicyclePlans = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            HemicyclePlanSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.hemicyclePlans = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
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
                id: null
            };
        };
    });
