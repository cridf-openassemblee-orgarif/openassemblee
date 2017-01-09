'use strict';

angular.module('babylone14166App')
	.controller('PresenceEluDeleteController', function($scope, $modalInstance, entity, PresenceElu) {

        $scope.presenceElu = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            PresenceElu.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });