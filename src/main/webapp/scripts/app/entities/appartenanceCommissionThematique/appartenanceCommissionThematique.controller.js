'use strict';

angular.module('openassembleeApp')
    .controller('AppartenanceCommissionThematiqueController', function ($scope, $state, $modal, AppartenanceCommissionThematique, AppartenanceCommissionThematiqueSearch) {

        $scope.appartenanceCommissionThematiques = [];
        $scope.loadAll = function() {
            AppartenanceCommissionThematique.query(function(result) {
               $scope.appartenanceCommissionThematiques = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            AppartenanceCommissionThematiqueSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.appartenanceCommissionThematiques = result;
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
            $scope.appartenanceCommissionThematique = {
                dateDebut: null,
                dateFin: null,
                motifFin: null,
                id: null
            };
        };
    });
