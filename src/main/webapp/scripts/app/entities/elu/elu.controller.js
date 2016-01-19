'use strict';

angular.module('babylone14166App')
    .controller('EluController', function ($scope, $state, $modal, Elu, EluSearch) {
      
        $scope.elus = [];
        $scope.loadAll = function() {
            Elu.query(function(result) {
               $scope.elus = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            EluSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.elus = result;
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
            $scope.elu = {
                civilite: null,
                nom: null,
                prenom: null,
                nomJeuneFille: null,
                profession: null,
                dateNaissance: null,
                lieuNaissance: null,
                id: null
            };
        };
    });
