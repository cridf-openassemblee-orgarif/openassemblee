'use strict';

angular.module('babylone14166App')
    .controller('ReunionCommissionThematiqueController', function ($scope, $state, $modal, ReunionCommissionThematique, ReunionCommissionThematiqueSearch, ParseLinks) {
      
        $scope.reunionCommissionThematiques = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            ReunionCommissionThematique.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.reunionCommissionThematiques = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            ReunionCommissionThematiqueSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.reunionCommissionThematiques = result;
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
            $scope.reunionCommissionThematique = {
                libelle: null,
                date: null,
                heureDebut: null,
                heureFin: null,
                id: null
            };
        };
    });
