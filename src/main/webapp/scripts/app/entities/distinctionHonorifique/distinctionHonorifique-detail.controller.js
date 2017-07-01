'use strict';

angular.module('openassembleeApp')
    .controller('DistinctionHonorifiqueDetailController', function ($scope, $rootScope, $stateParams, entity, DistinctionHonorifique, Elu) {
        $scope.distinctionHonorifique = entity;
        $scope.load = function (id) {
            DistinctionHonorifique.get({id: id}, function(result) {
                $scope.distinctionHonorifique = result;
            });
        };
        var unsubscribe = $rootScope.$on('openassembleeApp:distinctionHonorifiqueUpdate', function(event, result) {
            $scope.distinctionHonorifique = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
