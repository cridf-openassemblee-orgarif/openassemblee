'use strict';

angular.module('openassembleeApp')
    .controller('PresenceEluController', function ($scope, $state, $modal, PresenceElu, PresenceEluSearch, ParseLinks) {

        $scope.presenceElus = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            PresenceElu.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.presenceElus = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            PresenceEluSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.presenceElus = result;
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
            $scope.presenceElu = {
                id: null
            };
        };
    });
