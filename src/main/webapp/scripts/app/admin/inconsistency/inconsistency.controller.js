'use strict';

angular.module('openassembleeApp')
    .controller('InconsistencyController', function ($scope, InconsistencyService) {
        $scope.inconsistencies = {};
        $scope.updatingMetrics = true;

        $scope.refresh = function () {
            $scope.updatingMetrics = true;
            InconsistencyService.get().then(function (promise) {
                $scope.inconsistencies = promise;
                $scope.updatingMetrics = false;
            });
        };

        $scope.refresh();

    });
