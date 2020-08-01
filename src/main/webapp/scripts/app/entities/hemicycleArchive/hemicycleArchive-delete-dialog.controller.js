'use strict';

angular.module('openassembleeApp')
	.controller('HemicycleArchiveDeleteController', function($scope, $modalInstance, entity, HemicycleArchive) {

        $scope.hemicycleArchive = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            HemicycleArchive.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });