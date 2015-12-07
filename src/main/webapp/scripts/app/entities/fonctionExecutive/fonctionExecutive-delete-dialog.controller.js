'use strict';

angular.module('babylone14166App')
	.controller('FonctionExecutiveDeleteController', function($scope, $modalInstance, entity, FonctionExecutive) {

        $scope.fonctionExecutive = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            FonctionExecutive.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });