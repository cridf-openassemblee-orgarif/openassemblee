'use strict';

angular.module('babylone14166App')
    .controller('ElusController', function ($scope, $state, $modal, Elu, EluSearch) {

        $scope.dtos = [];
        $scope.loadAll = function () {
            Elu.query(function (dtos) {
                $scope.dtos = dtos;
            });
        };
        $scope.loadAll();

        $scope.search = function () {
            EluSearch.query({query: $scope.searchQuery}, function (result) {
                $scope.dtos = result;
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
