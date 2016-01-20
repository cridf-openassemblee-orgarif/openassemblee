'use strict';

angular.module('babylone14166App')
    .directive('adresses', function () {
        return {
            restrict: 'E',
            templateUrl: 'scripts/app/components/adresses-component.html'
        }
    })
    .directive('groupesPolitiques', function () {
        return {
            restrict: 'E',
            templateUrl: 'scripts/app/components/groupes-politiques-component.html'
        }
    })
    .directive('commissionPermanente', function () {
        return {
            restrict: 'E',
            templateUrl: 'scripts/app/components/commission-permanente-component.html'
        }
    })
    .directive('commissionsThematiques', function () {
        return {
            restrict: 'E',
            templateUrl: 'scripts/app/components/commissions-thematiques-component.html'
        }
    })
    .directive('organismes', function () {
        return {
            restrict: 'E',
            templateUrl: 'scripts/app/components/organismes-component.html'
        }
    })
    .directive('viePolitiquePassee', function () {
        return {
            restrict: 'E',
            templateUrl: 'scripts/app/components/vie-politique-passee-component.html'
        }
    })
