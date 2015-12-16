'use strict';

angular.module('babylone14166App')
    .controller('FonctionCommissionThematiqueController', function ($scope, $state, $modal, FonctionCommissionThematique, FonctionCommissionThematiqueSearch) {
      
        $scope.fonctionCommissionThematiques = [];
        $scope.loadAll = function() {
            FonctionCommissionThematique.query(function(result) {
               $scope.fonctionCommissionThematiques = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            FonctionCommissionThematiqueSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.fonctionCommissionThematiques = result;
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
            $scope.fonctionCommissionThematique = {
                fonction: null,
                dateDebut: null,
                dateFin: null,
                motifFin: null,
                id: null
            };
        };
    });
