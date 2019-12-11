'use strict';

angular.module('openassembleeApp')
    .controller('AppartenanceGroupePolitiqueDeleteController', function($scope, $modalInstance, entity, AppartenanceGroupePolitique) {

        $scope.appartenanceGroupePolitique = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            AppartenanceGroupePolitique.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });
