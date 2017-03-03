'use strict';

angular.module('babylone14166App')
    .controller('CommissionPermanenteController', function ($scope, entity) {
        $scope.comm = {};
        if(entity.$promise) {
            entity.$promise.then(function callback() {
                init();
            });
        } else {
            init();
        }
        var init = function () {
            $scope.comm = {
                fes: entity.fonctionsExecutives.filter(function(a) {return a.dateFin !== null}),
                as: entity.appartenances.filter(function(a) {return a.dateFin !== null}),
                fs: entity.fonctions.filter(function(a) {return a.dateFin !== null})
            };
            console.log($scope.comm)
        }
    });
