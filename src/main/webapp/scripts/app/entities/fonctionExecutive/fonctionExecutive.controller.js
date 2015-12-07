'use strict';

angular.module('babylone14166App')
    .controller('FonctionExecutiveController', function ($scope, $state, $modal, FonctionExecutive, FonctionExecutiveSearch) {
      
        $scope.fonctionExecutives = [];
        $scope.loadAll = function() {
            FonctionExecutive.query(function(result) {
               $scope.fonctionExecutives = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            FonctionExecutiveSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.fonctionExecutives = result;
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
            $scope.fonctionExecutive = {
                id: null
            };
        };
    });
