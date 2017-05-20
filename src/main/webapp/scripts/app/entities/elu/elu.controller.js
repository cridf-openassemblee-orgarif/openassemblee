'use strict';

angular.module('openassembleeApp')
    .controller('EluController', function ($scope, $state, $modal, Elu, EluSearch) {

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
                motifDemission: null,
                dateDemission: null,
                id: null
            };
        };
    });
