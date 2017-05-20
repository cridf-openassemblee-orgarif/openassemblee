'use strict';

angular.module('openassembleeApp')
	.controller('SignatureDeleteController', function($scope, $modalInstance, entity, Signature) {

        $scope.signature = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Signature.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });
