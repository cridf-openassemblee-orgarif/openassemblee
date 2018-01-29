'use strict';

angular.module('openassembleeApp')
	.controller('NumeroFaxDeleteController', function($scope, $modalInstance, entity, Elu) {

        $scope.numeroFax = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (item) {
            Elu.deleteNumeroFax({eluId: item.eluId, numeroFaxId: item.numeroFaxId},
                function () {
                    $modalInstance.close(true);
                });
        };

    });
