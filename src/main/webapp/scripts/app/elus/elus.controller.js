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
