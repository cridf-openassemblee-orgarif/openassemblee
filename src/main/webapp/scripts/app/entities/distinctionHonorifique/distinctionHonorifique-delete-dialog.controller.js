'use strict';

angular.module('openassembleeApp')
	.controller('DistinctionHonorifiqueDeleteController', function($scope, $modalInstance, entity, DistinctionHonorifique) {

        $scope.distinctionHonorifique = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            DistinctionHonorifique.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });