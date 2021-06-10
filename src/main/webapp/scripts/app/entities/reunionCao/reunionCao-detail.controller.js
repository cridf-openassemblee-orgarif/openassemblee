"use strict";

angular
    .module("openassembleeApp")
    .controller(
        "ReunionCaoDetailController",
        function ($scope, $rootScope, $stateParams, entity, ReunionCao) {
            $scope.reunion = entity;
            $scope.signaturesCount = {
                totalMissing: 0,
            };

            $scope.updateSignaturesCount = function () {
                if (entity.presenceElus) {
                    var signaturesCountTotal = 0;
                    var awaited = entity.presenceElus.length;
                    entity.presenceElus.forEach(function (pe) {
                        signaturesCountTotal += pe.signatures.length;
                    });
                    $scope.signaturesCount.totalMissing =
                        awaited - signaturesCountTotal;
                }
            };

            $scope.$watch("reunion.presenceElus", function () {
                $scope.updateSignaturesCount();
            });

            $scope.load = function (id) {
                ReunionCao.get({ id: id }, function (result) {
                    $scope.reunion = result;
                });
            };
            var unsubscribe = $rootScope.$on(
                "openassembleeApp:reunionCaoUpdate",
                function (event, result) {
                    $scope.reunion = result;
                }
            );
            $scope.$on("$destroy", unsubscribe);
        }
    );
