"use strict";

angular
    .module("openassembleeApp")
    .directive("editButton", function () {
        return {
            restrict: "E",
            templateUrl: "scripts/app/components/edit-button.html",
        };
    })
    .directive("deleteButton", function () {
        return {
            restrict: "E",
            templateUrl: "scripts/app/components/delete-button.html",
        };
    })
    .directive("adressesPostales", function () {
        return {
            restrict: "E",
            templateUrl:
                "scripts/app/components/adresses-postales-component.html",
        };
    })
    .directive("groupesPolitiques", function () {
        return {
            restrict: "E",
            templateUrl:
                "scripts/app/components/groupes-politiques-component.html",
        };
    })
    .directive("commissionPermanente", function () {
        return {
            restrict: "E",
            templateUrl:
                "scripts/app/components/commission-permanente-component.html",
        };
    })
    .directive("commissionsThematiques", function () {
        return {
            restrict: "E",
            templateUrl:
                "scripts/app/components/commissions-thematiques-component.html",
        };
    })
    .directive("organismes", function () {
        return {
            restrict: "E",
            templateUrl: "scripts/app/components/organismes-component.html",
        };
    })
    .directive("autresMandats", function () {
        return {
            restrict: "E",
            templateUrl: "scripts/app/components/autres-mandats-component.html",
        };
    })
    .directive("viePolitiquePassee", function () {
        return {
            restrict: "E",
            templateUrl:
                "scripts/app/components/vie-politique-passee-component.html",
        };
    })
    .directive("adressesMail", function () {
        return {
            restrict: "E",
            templateUrl: "scripts/app/components/adresses-mail-component.html",
        };
    })
    .directive("identitesInternet", function () {
        return {
            restrict: "E",
            templateUrl:
                "scripts/app/components/identites-internet-component.html",
        };
    })
    .directive("numerosFax", function () {
        return {
            restrict: "E",
            templateUrl: "scripts/app/components/numeros-fax-component.html",
        };
    })
    .directive("numerosTelephone", function () {
        return {
            restrict: "E",
            templateUrl:
                "scripts/app/components/numeros-telephone-component.html",
        };
    })
    .directive("signatureSeance", function () {
        return {
            restrict: "E",
            link: function ($scope, elem, attrs) {
                if (attrs.signature) {
                    $scope.signature = JSON.parse(attrs.signature);
                } else {
                    $scope.signature = {};
                }
            },
            controller: [
                "$scope",
                "Signature",
                function SignatureSeanceController($scope, Signature) {
                    $scope.color = function (color) {
                        switch (color) {
                            case "PRESENT":
                                return "#bff1c5";
                            case "ABSENT":
                                return "#f1b294";
                            case "EXCUSE":
                                return "#f1e8b5";
                        }
                        return "#bbb";
                    };

                    $scope.requesting = false;

                    $scope.$watch("presenteElu", function () {
                        $scope.signature = $scope.presenceElu.signatures.find(
                            function (s) {
                                return s.position === $scope.index;
                            }
                        );
                    });

                    $scope.setSignature = function (statut) {
                        $scope.requesting = true;
                        // $scope.signature peut être déjà un objet, et si était inexistant, alors angular l'a instancié du fait
                        // du binding
                        if ($scope.signature.id) {
                            Signature.update($scope.signature).$promise.then(
                                function (result) {
                                    $scope.requesting = false;
                                    // $scope.signature est une construction locale, on remet son état dans le presenceElu qui est
                                    // le "vrai" model, notamment pour que le filtre sur les signature dans le tableau global puisse
                                    // fontionner (ainsi que les comptes...)
                                    $scope.presenceElu.signatures.forEach(
                                        function (s) {
                                            if (s.id === $scope.signature.id) {
                                                s.statut =
                                                    $scope.signature.statut;
                                            }
                                        }
                                    );
                                    $scope.updateSignaturesCount();
                                }
                            );
                        } else {
                            // $scope.signature a été instancié mais ne contient pas pour autant la bonne position, le bon
                            // id élu...
                            var signature = $scope.signature;
                            signature.position = $scope.index;
                            signature.presenceElu = {
                                id: $scope.presenceElu.id,
                            };
                            Signature.save(signature).$promise.then(function (
                                result
                            ) {
                                $scope.requesting = false;
                                $scope.signature.id = result.id;
                                // de la même façon que dans le cas où la signature existait il fallait remettre l'état
                                // dans le "vrai" model, ici il faut rajouter la signature dans ce model
                                $scope.presenceElu.signatures.push(result);
                                $scope.updateSignaturesCount();
                            });
                        }
                    };
                },
            ],
            templateUrl: "scripts/app/components/signature-seance.html",
        };
    })
    // thanks to https://www.grobmeier.de/bootstrap-tabs-with-angular-js-25112012.html
    .directive("showtab", function () {
        return {
            link: function (scope, element, attrs) {
                element.click(function (e) {
                    e.preventDefault();
                    $(element).tab("show");
                });
            },
        };
    });
