"use strict";

angular
    .module("openassembleeApp")
    .controller("EluController", function ($scope, entity) {
        $scope.dtos = entity;
        $scope.elus = [];
        $scope.anciens = [];

        $scope.$watch("dtos", function () {
            $scope.dtos.$promise.then(function (dtos) {
                // simpler de faire le chunks ici à cause de la promesse, et fait qu'on recup directement le array
                $scope.elus = dtos.filter(function (dto) {
                    return !dto.elu.dateDemission;
                });
                $scope.nombreElus = $scope.elus.length;
                $scope.anciens = dtos.filter(function (dto) {
                    return dto.elu.dateDemission;
                });
            });
        });
    });
