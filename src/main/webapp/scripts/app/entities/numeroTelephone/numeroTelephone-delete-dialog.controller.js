"use strict";

angular
    .module("openassembleeApp")
    .controller(
        "NumeroTelephoneDeleteController",
        function ($scope, $modalInstance, entity, Elu) {
            $scope.numeroTelephone = entity;
            $scope.clear = function () {
                $modalInstance.dismiss("cancel");
            };
            $scope.confirmDelete = function (item) {
                Elu.deleteNumeroTelephone(
                    {
                        eluId: item.eluId,
                        numeroTelephoneId: item.numeroTelephoneId,
                    },
                    function () {
                        $modalInstance.close(true);
                    }
                );
            };
        }
    );
