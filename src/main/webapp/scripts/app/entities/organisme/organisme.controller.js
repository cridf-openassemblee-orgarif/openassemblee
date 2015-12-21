'use strict';

angular.module('babylone14166App')
    .controller('OrganismeController', function ($scope, $state, $modal, Organisme, OrganismeSearch) {
      
        $scope.organismes = [];
        $scope.loadAll = function() {
            Organisme.query(function(result) {
               $scope.organismes = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            OrganismeSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.organismes = result;
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
            $scope.organisme = {
                nom: null,
                codeRNE: null,
                siret: null,
                sigle: null,
                type: null,
                secteur: null,
                dateDebut: null,
                dateFin: null,
                motifFin: null,
                id: null
            };
        };
    });
