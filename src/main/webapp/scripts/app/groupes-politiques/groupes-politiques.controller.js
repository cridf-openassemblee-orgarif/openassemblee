'use strict';

angular.module('babylone14166App')
    .controller('GroupesPolitiquesController', function ($scope, $state, $modal, GroupePolitique, GroupePolitiqueSearch) {

        $scope.groupesPolitiques = [];
        $scope.loadAll = function () {
            GroupePolitique.query(function (result) {
                $scope.groupesPolitiques = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            GroupePolitiqueSearch.query({query: $scope.searchQuery}, function (result) {
                $scope.groupesPolitiques = result;
            }, function (response) {
                if (response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.groupesPolitique = {
                nom: null,
                nomCourt: null,
                dateDebut: null,
                dateFin: null,
                motifFin: null,
                id: null
            };
        };
    });
