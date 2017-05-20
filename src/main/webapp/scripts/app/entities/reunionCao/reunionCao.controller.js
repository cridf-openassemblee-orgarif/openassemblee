'use strict';

angular.module('openassembleeApp')
    .controller('ReunionCaoController', function ($scope, $state, $modal, ReunionCao, ReunionCaoSearch, ParseLinks) {

        $scope.reunionCaos = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            ReunionCao.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.reunionCaos = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            ReunionCaoSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.reunionCaos = result;
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
            $scope.reunionCao = {
                libelle: null,
                date: null,
                heureDebut: null,
                heureFin: null,
                id: null
            };
        };
    });
