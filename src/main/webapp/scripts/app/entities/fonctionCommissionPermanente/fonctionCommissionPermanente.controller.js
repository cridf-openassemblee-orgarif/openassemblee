'use strict';

angular.module('babylone14166App')
    .controller('FonctionCommissionPermanenteController', function ($scope, $state, $modal, FonctionCommissionPermanente, FonctionCommissionPermanenteSearch) {
      
        $scope.fonctionCommissionPermanentes = [];
        $scope.loadAll = function() {
            FonctionCommissionPermanente.query(function(result) {
               $scope.fonctionCommissionPermanentes = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            FonctionCommissionPermanenteSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.fonctionCommissionPermanentes = result;
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
            $scope.fonctionCommissionPermanente = {
                fonction: null,
                dateDebut: null,
                dateFin: null,
                motifFin: null,
                id: null
            };
        };
    });
