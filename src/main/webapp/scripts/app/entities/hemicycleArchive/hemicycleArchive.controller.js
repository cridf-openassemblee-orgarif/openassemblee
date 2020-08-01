'use strict';

angular.module('openassembleeApp')
    .controller('HemicycleArchiveController', function ($scope, $state, $modal, HemicycleArchive, HemicycleArchiveSearch) {
      
        $scope.hemicycleArchives = [];
        $scope.loadAll = function() {
            HemicycleArchive.query(function(result) {
               $scope.hemicycleArchives = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            HemicycleArchiveSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.hemicycleArchives = result;
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
            $scope.hemicycleArchive = {
                jsonPlan: null,
                svgPlan: null,
                date: null,
                id: null
            };
        };
    });
