'use strict';

angular.module('babylone14166App')
    .controller('GroupesPolitiquesController', function ($scope, $state, $modal, $http,
                                                         GroupePolitique, GroupePolitiqueSearch) {

        $scope.dtos = [];
        $scope.loadAll = function () {
            $http({
                method: 'GET',
                url: 'api/groupePolitiques-dtos'
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
            $scope.groupesPolitique = {
                nom: null,
                nomCourt: null,
                dateDebut: null,
                dateFin: null,
                motifFin: null,
                website: null,
                phone: null,
                mail: null,
                fax: null,
                id: null
            };
        };
    });
