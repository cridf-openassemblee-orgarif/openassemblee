'use strict';

angular.module('babylone14166App')
    .controller('AppartenanceOrganismeController', function ($scope, $state, $modal, AppartenanceOrganisme, AppartenanceOrganismeSearch) {
      
        $scope.appartenanceOrganismes = [];
        $scope.loadAll = function() {
            AppartenanceOrganisme.query(function(result) {
               $scope.appartenanceOrganismes = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            AppartenanceOrganismeSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.appartenanceOrganismes = result;
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
            $scope.appartenanceOrganisme = {
                statut: null,
                codeRNE: null,
                dateDebut: null,
                dateFin: null,
                motifFin: null,
                dateNomination: null,
                reference: null,
                type: null,
                lienPiece: null,
                id: null
            };
        };
    });
