'use strict';

angular.module('openassembleeApp')
    .controller('FonctionCommissionThematiqueDetailController', function ($scope, $rootScope, $stateParams, entity, FonctionCommissionThematique, Elu, CommissionThematique) {
        $scope.fonctionCommissionThematique = entity;
        $scope.load = function (id) {
            FonctionCommissionThematique.get({id: id}, function(result) {
                $scope.fonctionCommissionThematique = result;
            });
        };
        var unsubscribe = $rootScope.$on('openassembleeApp:fonctionCommissionThematiqueUpdate', function(event, result) {
            $scope.fonctionCommissionThematique = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
