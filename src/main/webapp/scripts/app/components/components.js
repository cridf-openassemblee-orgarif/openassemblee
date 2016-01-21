'use strict';

angular.module('babylone14166App')
    .directive('adressesPostales', function () {
        return {
            restrict: 'E',
            templateUrl: 'scripts/app/components/adresses-postales-component.html'
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
    .directive('adressesMail', function () {
        return {
            restrict: 'E',
            templateUrl: 'scripts/app/components/adresses-mail-component.html'
        }
    })
    .directive('identitesInternet', function () {
        return {
            restrict: 'E',
            templateUrl: 'scripts/app/components/identites-internet-component.html'
        }
    })
    .directive('numerosFax', function () {
        return {
            restrict: 'E',
            templateUrl: 'scripts/app/components/numeros-fax-component.html'
        }
    })
    .directive('numerosTelephone', function () {
        return {
            restrict: 'E',
            templateUrl: 'scripts/app/components/numeros-telephone-component.html'
        }
    })
