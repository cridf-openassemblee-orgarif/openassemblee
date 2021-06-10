"use strict";

angular
    .module("openassembleeApp")
    .controller(
        "AdresseMailDeleteController",
        function ($scope, $modalInstance, entity, Elu) {
            $scope.adresseMail = entity;
            $scope.clear = function () {
                $modalInstance.dismiss("cancel");
            };
            $scope.confirmDelete = function (item) {
                Elu.deleteAdresseMail(
                    { eluId: item.eluId, adresseMailId: item.adresseMailId },
                    function () {
                        $modalInstance.close(true);
                    }
                );
            };
        }
    );
