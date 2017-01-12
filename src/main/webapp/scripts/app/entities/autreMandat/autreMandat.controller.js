'use strict';

angular.module('babylone14166App')
    .controller('AutreMandatController', function ($scope, $state, $modal, AutreMandat, AutreMandatSearch) {
      
        $scope.autreMandats = [];
        $scope.loadAll = function() {
            AutreMandat.query(function(result) {
               $scope.autreMandats = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            AutreMandatSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.autreMandats = result;
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
            $scope.autreMandat = {
                collectiviteOuOrganisme: null,
                fonction: null,
                dateDebut: null,
                dateFin: null,
                motifFin: null,
                id: null
            };
        };
    });
