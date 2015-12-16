'use strict';

angular.module('babylone14166App')
    .controller('FonctionCommissionThematiqueDetailController', function ($scope, $rootScope, $stateParams, entity, FonctionCommissionThematique, Elu, CommissionThematique) {
        $scope.fonctionCommissionThematique = entity;
        $scope.load = function (id) {
            FonctionCommissionThematique.get({id: id}, function(result) {
                $scope.fonctionCommissionThematique = result;
            });
        };
        var unsubscribe = $rootScope.$on('babylone14166App:fonctionCommissionThematiqueUpdate', function(event, result) {
            $scope.fonctionCommissionThematique = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
