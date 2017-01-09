'use strict';

angular.module('babylone14166App')
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