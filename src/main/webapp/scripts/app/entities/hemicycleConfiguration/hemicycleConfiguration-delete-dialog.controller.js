'use strict';

angular.module('openassembleeApp')
	.controller('HemicycleConfigurationDeleteController', function($scope, $modalInstance, entity, HemicycleConfiguration) {

        $scope.hemicycleConfiguration = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            HemicycleConfiguration.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });