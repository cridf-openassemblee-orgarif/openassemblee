'use strict';

angular.module('openassembleeApp')
	.controller('ReunionCaoDeleteController', function($scope, $modalInstance, entity, ReunionCao) {

        $scope.reunionCao = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            ReunionCao.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });
