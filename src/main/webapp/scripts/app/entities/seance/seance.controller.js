'use strict';

angular.module('babylone14166App')
    .controller('SeanceController', function ($scope, $state, $modal, Seance, SeanceSearch, ParseLinks) {

        $scope.seances = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Seance.query({page: $scope.page, size: 20, sort: 'date,desc'}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.seances = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            SeanceSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.seances = result;
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
            $scope.seance = {
                intitule: null,
                type: null,
                date: null,
                nombreSignatures: null,
                id: null
            };
        };
    });
