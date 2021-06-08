'use strict';

angular.module('openassembleeApp')
    .controller('ListeElectoraleController', function ($scope, $state, $modal, ListeElectorale, ListeElectoraleSearch) {
      
        $scope.listeElectorales = [];
        $scope.loadAll = function() {
            ListeElectorale.query(function(result) {
               $scope.listeElectorales = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            ListeElectoraleSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.listeElectorales = result;
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
            $scope.listeElectorale = {
                nom: null,
                nomCourt: null,
                id: null
            };
        };
    });
