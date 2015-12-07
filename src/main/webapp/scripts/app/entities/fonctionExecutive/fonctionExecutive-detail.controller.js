'use strict';

angular.module('babylone14166App')
    .controller('FonctionExecutiveDetailController', function ($scope, $rootScope, $stateParams, entity, FonctionExecutive, Elu) {
        $scope.fonctionExecutive = entity;
        $scope.load = function (id) {
            FonctionExecutive.get({id: id}, function(result) {
                $scope.fonctionExecutive = result;
            });
        };
        var unsubscribe = $rootScope.$on('babylone14166App:fonctionExecutiveUpdate', function(event, result) {
            $scope.fonctionExecutive = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
