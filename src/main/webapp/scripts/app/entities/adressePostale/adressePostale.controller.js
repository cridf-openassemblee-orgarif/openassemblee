'use strict';

angular.module('babylone14166App')
    .controller('AdressePostaleController', function ($scope, $state, $modal, AdressePostale, AdressePostaleSearch) {
      
        $scope.adressePostales = [];
        $scope.loadAll = function() {
            AdressePostale.query(function(result) {
               $scope.adressePostales = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            AdressePostaleSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.adressePostales = result;
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
            $scope.adressePostale = {
                natureProPerso: null,
                rue: null,
                codePostal: null,
                ville: null,
                niveauConfidentialite: null,
                adresseDeCorrespondance: null,
                publicationAnnuaire: null,
                id: null
            };
        };
    });
