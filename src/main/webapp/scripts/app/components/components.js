'use strict';

angular.module('babylone14166App')
    .directive('eluListItem', function () {
        return {
            restrict: 'E',
            templateUrl: 'scripts/app/components/elu-list-item-component.html'
        }
    })
    .directive('viePolitique', function () {
        return {
            restrict: 'E',
            templateUrl: 'scripts/app/components/vie-politique-component.html',
        }
    })
    .directive('identiteEdit', function () {
        return {
            restrict: 'E',
            templateUrl: 'scripts/app/components/identite-edit-component.html'
        }
    })
    .directive('adressePostaleEdit', function () {
        return {
            restrict: 'E',
            templateUrl: 'scripts/app/components/adresse-postale-edit-component.html'
        }
    })
    .directive('numeroTelephoneEdit', function () {
        return {
            restrict: 'E',
            templateUrl: 'scripts/app/components/numero-telephone-edit-component.html'
        }
    })
    .directive('numeroFaxEdit', function () {
        return {
            restrict: 'E',
            templateUrl: 'scripts/app/components/numero-fax-edit-component.html'
        }
    })
    .directive('adresseMailEdit', function () {
        return {
            restrict: 'E',
            templateUrl: 'scripts/app/components/adresse-mail-edit-component.html'
        }
    })
    .directive('identiteInternetEdit', function () {
        return {
            restrict: 'E',
            templateUrl: 'scripts/app/components/identite-internet-edit-component.html'
        }
    })
    .directive('groupePolitique', function () {
        return {
            restrict: 'E',
            templateUrl: 'scripts/app/components/groupe-politique-component.html'
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

