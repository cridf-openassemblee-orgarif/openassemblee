'use strict';

angular.module('openassembleeApp')
	.controller('SeanceDeleteController', function($scope, $modalInstance, entity, Seance) {

        $scope.seance = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Seance.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });
