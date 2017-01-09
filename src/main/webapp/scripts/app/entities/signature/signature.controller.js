'use strict';

angular.module('babylone14166App')
    .controller('SignatureController', function ($scope, $state, $modal, Signature, SignatureSearch, ParseLinks) {
      
        $scope.signatures = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Signature.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.signatures = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            SignatureSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.signatures = result;
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
            $scope.signature = {
                position: null,
                statut: null,
                id: null
            };
        };
    });
