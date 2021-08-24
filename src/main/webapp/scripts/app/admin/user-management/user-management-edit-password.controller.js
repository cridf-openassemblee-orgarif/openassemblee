"use strict";

angular
    .module("openassembleeApp")
    .controller("UserManagementEditPasswordController", [
        "$scope",
        "$stateParams",
        "$modalInstance",
        "$http",
        function ($scope, $stateParams, $modalInstance, $http) {
            $scope.userPassword = {
                password: "",
            };

            $scope.save = function () {
                $scope.isSaving = true;
                if ($stateParams.id != null) {
                    $http
                        .put(
                            "/api/users/" +
                                $stateParams.id +
                                "/change-password",
                            $scope.userPassword.password
                        )
                        .then(function () {
                            $scope.isSaving = false;
                            $modalInstance.close();
                        });
                }
            };

            $scope.clear = function () {
                $modalInstance.dismiss("cancel");
            };
        },
    ]);
