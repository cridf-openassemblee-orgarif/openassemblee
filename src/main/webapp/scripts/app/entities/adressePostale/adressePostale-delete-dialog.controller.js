'use strict';

angular.module('babylone14166App')
	.controller('AdressePostaleDeleteController', function($scope, $modalInstance, entity, AdressePostale) {

        $scope.adressePostale = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            AdressePostale.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });