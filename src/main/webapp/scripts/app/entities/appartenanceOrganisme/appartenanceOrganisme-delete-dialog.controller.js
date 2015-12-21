'use strict';

angular.module('babylone14166App')
	.controller('AppartenanceOrganismeDeleteController', function($scope, $modalInstance, entity, AppartenanceOrganisme) {

        $scope.appartenanceOrganisme = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            AppartenanceOrganisme.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });