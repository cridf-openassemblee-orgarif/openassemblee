'use strict';

angular.module('openassembleeApp')
	.controller('IdentiteInternetDeleteController', function($scope, $modalInstance, entity, Elu) {

        $scope.identiteInternet = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (item) {
            Elu.deleteIdentiteInternet({eluId: item.eluId, identiteInternetId: item.identiteInternetId},
                function () {
                    $modalInstance.close(true);
                });
        };

    });
