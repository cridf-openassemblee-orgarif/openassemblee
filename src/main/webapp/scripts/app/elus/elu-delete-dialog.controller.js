'use strict';

angular.module('babylone14166App')
    .controller('EluDeleteController', function ($scope, $modalInstance, entity, Elu) {

        $scope.elu = entity;
        $scope.clear = function () {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Elu.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });
