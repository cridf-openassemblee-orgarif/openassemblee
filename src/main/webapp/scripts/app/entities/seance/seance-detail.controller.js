'use strict';

angular.module('openassembleeApp')
.controller('SeanceDetailController', function ($scope, $rootScope, $stateParams, entity, Seance) {
    $scope.dto = entity;
    $scope.pouvoirsOptions = {
        filter: 'tous',
        groupePolitique: 'tous'
    };
    $scope.groupePolitiques = {};
    $scope.filteredGroupePolitiques = {};
    $scope.signaturesArray = [];
    $scope.signaturesOptions = {
        filter: 'no-filter',
        signaturesNumber: 0,
        groupePolitique: 'no-filter'
    };
    $scope.signaturesCount = {
        totalMissing: 0,
        stats: {}
    };

    var initGroupePolitiques = function () {
        var groupesPolitiques = {};
        if ($scope.dto.pouvoirs) {
            $scope.dto.pouvoirs.map(function (p) {
                if (p.eluCedeur.groupePolitique != null && groupesPolitiques[p.eluCedeur.groupePolitique.id] == null) {
                    groupesPolitiques[p.eluCedeur.groupePolitique.id] = p.eluCedeur.groupePolitique;
                }
                if (p.eluBeneficiaire.groupePolitique != null && groupesPolitiques[p.eluBeneficiaire.groupePolitique.id] == null) {
                    groupesPolitiques[p.eluBeneficiaire.groupePolitique.id] = p.eluBeneficiaire.groupePolitique;
                }
            });
        }
        $scope.filteredGroupePolitiques = groupesPolitiques;
    };

    var initSignaturesArray = function () {
        if (entity.seance) {
            for (var i = 1; i <= entity.seance.nombreSignatures; i++) {
                $scope.signaturesArray.push(i);
            }
            $scope.signaturesOptions.signaturesNumber = entity.seance.nombreSignatures;
        }
    };

    $scope.updateSignaturesCount = function () {
        if (entity.seance) {
            var signaturesCountTotal = 0;
            var awaited = entity.seance.presenceElus.length * entity.seance.nombreSignatures;
            var stats = {};
            entity.groupePolitiques.forEach(function (gp) {
                stats[gp.nomCourt] = {}
            });
            for (var i = 1; i <= entity.seance.nombreSignatures; i++) {
                stats[i] = {};
                stats[i]['PRESENT'] = 0;
                stats[i]['ABSENT'] = 0;
                stats[i]['EXCUSE'] = 0;
                stats[i]['missing'] = 0;
                entity.groupePolitiques.forEach(function (gp) {
                    stats[gp.nomCourt][i] = {}
                    stats[gp.nomCourt][i]['PRESENT'] = 0;
                    stats[gp.nomCourt][i]['ABSENT'] = 0;
                    stats[gp.nomCourt][i]['EXCUSE'] = 0;
                    stats[gp.nomCourt][i]['missing'] = 0;
                });
            }
            entity.seance.presenceElus.forEach(function (pe) {
                if (!stats[pe.elu.groupePolitique]) {
                    stats[pe.elu.groupePolitique] = {};
                }
                signaturesCountTotal += pe.signatures.length;
                pe.signatures.forEach(function (s) {
                    stats[s.position][s.statut]++;
                    stats[pe.elu.groupePolitique][s.position][s.statut]++;
                });
            });
            for (var i = 1; i <= entity.seance.nombreSignatures; i++) {
                stats[i]['missing'] = entity.seance.presenceElus.length -
                    (stats[i]['PRESENT'] + stats[i]['ABSENT'] + stats[i]['EXCUSE']);
                entity.groupePolitiques.forEach(function (gp) {
                    stats[gp.nomCourt][i]['missing'] = entity.seance.presenceElus.length -
                        (stats[gp.nomCourt][i]['PRESENT'] + stats[gp.nomCourt][i]['ABSENT'] + stats[gp.nomCourt][i]['EXCUSE']);
                });
            }
            $scope.signaturesCount.totalMissing = awaited - signaturesCountTotal;
            $scope.signaturesCount.stats = stats;
        }
    };

    $scope.$watch('dto.pouvoirs', function () {
        initGroupePolitiques();
        initSignaturesArray();
        $scope.updateSignaturesCount();
    });

    $scope.load = function (id) {
        Seance.getDto({id: id}, function (result) {
            $scope.dto = result;
        });
    };
    var unsubscribe = $rootScope.$on('openassembleeApp:seanceUpdate', function (event, result) {
        $scope.seance = result;
    });
    $scope.$on('$destroy', unsubscribe);

});
