'use strict';

angular.module('babylone14166App')
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
                nomCourt: null,
                dateDebut: null,
                dateFin: null,
                motifFin: null,
                id: null
            };
        };
    });
