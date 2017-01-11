'use strict';

angular.module('babylone14166App')
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