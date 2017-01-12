'use strict';

angular.module('babylone14166App')
	.controller('AutreMandatDeleteController', function($scope, $modalInstance, entity, AutreMandat) {

        $scope.autreMandat = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            AutreMandat.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });