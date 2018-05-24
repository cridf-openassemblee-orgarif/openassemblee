'use strict';

angular.module('openassembleeApp')
.directive('editButton', function () {
    return {
        restrict: 'E',
        templateUrl: 'scripts/app/components/edit-button.html'
    }
})
.directive('deleteButton', function () {
    return {
        restrict: 'E',
        templateUrl: 'scripts/app/components/delete-button.html'
    }
})
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
.directive('autresMandats', function () {
    return {
        restrict: 'E',
        templateUrl: 'scripts/app/components/autres-mandats-component.html'
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
.directive('signatureSeance', function () {
    return {
        restrict: 'E',
        link: function ($scope, elem, attrs) {
            if (attrs.signature) {
                $scope.signature = JSON.parse(attrs.signature);
            } else {
                $scope.signature = {};
            }
        },
        controller: ['$scope', 'Signature', function SignatureSeanceController($scope, Signature) {
            $scope.color = function (color) {
                switch (color) {
                    case 'PRESENT':
                        return '#bff1c5';
                    case 'ABSENT':
                        return '#f1b294';
                    case 'EXCUSE':
                        return '#f1e8b5';
                }
                return '#bbb';
            };
            $scope.requesting = false;
            $scope.setSignature = function (signature) {
                $scope.requesting = true;
                if (signature.id) {
                    Signature.update(signature).$promise.then(function (result) {
                        $scope.requesting = false;
                        $scope.signature.id = result.id;
                    });
                } else {
                    Signature.save(signature).$promise.then(function (result) {
                        $scope.requesting = false;
                        $scope.signature.id = result.id;
                    });
                }
            };
        }],
        templateUrl: 'scripts/app/components/signature-seance.html'
    }
})
// thanks to https://www.grobmeier.de/bootstrap-tabs-with-angular-js-25112012.html
.directive('showtab',
    function () {
        return {
            link: function (scope, element, attrs) {
                element.click(function (e) {
                    e.preventDefault();
                    $(element).tab('show');
                });
            }
        };
    });
