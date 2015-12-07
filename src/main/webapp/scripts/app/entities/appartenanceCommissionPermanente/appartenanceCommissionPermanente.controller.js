'use strict';

angular.module('babylone14166App')
    .controller('AppartenanceCommissionPermanenteController', function ($scope, $state, $modal, AppartenanceCommissionPermanente, AppartenanceCommissionPermanenteSearch) {
      
        $scope.appartenanceCommissionPermanentes = [];
        $scope.loadAll = function() {
            AppartenanceCommissionPermanente.query(function(result) {
               $scope.appartenanceCommissionPermanentes = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            AppartenanceCommissionPermanenteSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.appartenanceCommissionPermanentes = result;
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
            $scope.appartenanceCommissionPermanente = {
                dateDebut: null,
                dateFin: null,
                motifFin: null,
                id: null
            };
        };
    });
