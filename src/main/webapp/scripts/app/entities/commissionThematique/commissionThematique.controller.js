'use strict';

angular.module('babylone14166App')
    .controller('CommissionThematiqueController', function ($scope, $state, $modal, CommissionThematique, CommissionThematiqueSearch) {

        $scope.commissionThematiques = [];
        $scope.loadAll = function() {
            CommissionThematique.query(function(result) {
                $scope.dtos = result;
            });
        };
        $scope.loadAll();

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.commissionThematique = {
                nom: null,
                nomCourt: null,
                dateDebut: null,
                dateFin: null,
                motifFin: null,
                id: null
            };
        };
    });
