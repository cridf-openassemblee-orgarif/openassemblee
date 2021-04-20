'use strict';

angular.module('openassembleeApp')
	.controller('MandatureDeleteController', function($scope, $modalInstance, entity, Mandature) {

        $scope.mandature = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Mandature.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });