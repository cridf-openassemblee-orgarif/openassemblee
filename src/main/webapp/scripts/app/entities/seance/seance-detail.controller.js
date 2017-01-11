'use strict';

angular.module('babylone14166App')
    .controller('SeanceDetailController', function ($scope, $rootScope, $stateParams, entity, Seance) {
        $scope.dto = entity;
        $scope.signaturesArray = [];

        var initSignaturesArray = function () {
            for (var i = 1; i <= entity.seance.nombreSignatures; i++) {
                $scope.signaturesArray.push(i);
            }
        };

        if (entity.$promise) {
            entity.$promise.then(function callback() {
                initSignaturesArray()
            });
        } else {
            initSignaturesArray()
        }

        $scope.getSignature = function (presenceElu, position) {
            return presenceElu.signatures.find(function (s) {
                return s.position == position;
            });
        };

        $scope.computeGroupesPolitiques = function () {
            var groupesPolitiques = {};
            $scope.dto.pouvoirs.map(function (p) {
                if (p.eluCedeur.groupePolitique != null && groupesPolitiques[p.eluCedeur.groupePolitique.id] == null) {
                    groupesPolitiques[p.eluCedeur.groupePolitique.id] = p.eluCedeur.groupePolitique;
                }
                if (p.eluBeneficiaire.groupePolitique != null && groupesPolitiques[p.eluBeneficiaire.groupePolitique.id] == null) {
                    groupesPolitiques[p.eluBeneficiaire.groupePolitique.id] = p.eluBeneficiaire.groupePolitique;
                }
            });
            return groupesPolitiques;
        };

        $scope.computePouvoirs = function () {
            return $scope.dto.pouvoirs
                .filter(function (pvDto) {
                    switch ($scope.filtre.state) {
                        case 'tous':
                            return true;
                        case 'actifs':
                            return pvDto.pouvoir.dateFin == null || pvDto.pouvoir.heureFin == null;
                        case 'nonActifs':
                            return pvDto.pouvoir.dateFin != null && pvDto.pouvoir.heureFin != null;
                    }
                    return true;
                })
                .filter(function (pvDto) {
                    if ($scope.filtre.groupePolitique == 'tous') {
                        return true;
                    }
                    var gpId = $scope.filtre.groupePolitique;
                    return (pvDto.eluCedeur.groupePolitique != null && pvDto.eluCedeur.groupePolitique.id == gpId)
                        || (pvDto.eluBeneficiaire.groupePolitique != null && pvDto.eluBeneficiaire.groupePolitique.id == gpId);
                });
        };

        $scope.filtre = {
            state: 'tous',
            groupePolitique: 'tous'
        };

        $scope.load = function (id) {
            Seance.getDto({id: id}, function (result) {
                $scope.dto = result;
            });
        };
        var unsubscribe = $rootScope.$on('babylone14166App:seanceUpdate', function (event, result) {
            $scope.seance = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
