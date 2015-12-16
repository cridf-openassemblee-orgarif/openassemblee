'use strict';

angular.module('babylone14166App')
    .controller('CommissionThematiqueController', function ($scope, $state, $modal, CommissionThematique, CommissionThematiqueSearch) {
      
        $scope.commissionThematiques = [];
        $scope.loadAll = function() {
            CommissionThematique.query(function(result) {
               $scope.commissionThematiques = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            CommissionThematiqueSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.commissionThematiques = result;
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
            $scope.commissionThematique = {
                nom: null,
                nomCourt: null,
                dateDebut: null,
                dateFin: null,
                motifFin: null,
                id: null
            };
        };
    });
