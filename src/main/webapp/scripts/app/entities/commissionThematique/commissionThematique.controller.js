'use strict';

angular.module('openassembleeApp')
    .controller('CommissionThematiqueController', function ($scope, $state, $modal, $http,
                                                            CommissionThematique, CommissionThematiqueSearch) {

        $scope.commissionThematiques = [];
        $scope.loadAll = function() {
            $http({
                method: 'GET',
                url: 'api/commissionThematiques-dtos'
            }).then(function successCallback(result) {
                $scope.dtos = result.data;
            }, function errorCallback(response) {
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
                dateDebut: null,
                dateFin: null,
                motifFin: null,
                id: null
            };
        };
    });
