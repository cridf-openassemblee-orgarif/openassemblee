'use strict';

angular.module('openassembleeApp').controller('HemicyclePlanDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'HemicyclePlan', 'HemicycleConfiguration', 'Seance', '$http',
        function ($scope, $stateParams, $modalInstance, entity, HemicyclePlan, HemicycleConfiguration, Seance, $http) {
            $scope.hemicyclePlan = entity;
            $scope.properties = {
                fromAlphabeticOrder: false,
                fromSeance: undefined,
                fromProjet: undefined
            };
            $scope.hemicycleConfigurations = [];
            HemicycleConfiguration.query(function (result) {
                $scope.hemicycleConfigurations = result.filter(function (c) {
                    return c.frozen;
                });
                $scope.hemicyclePlan.configuration = $scope.hemicycleConfigurations[0];
            });
            // FIXMENOW juste choper les dernières séances
            $scope.seances = [{intitule: ''}];
            Seance.query(function(result) {
                angular.forEach(result, function (d) {
                    $scope.seances.push(d);
                });
            });
            $scope.projets = [{label: ''}];
            $http({
                method: 'GET',
                url: 'api/hemicyclePlans-projets'
            }).then(function successCallback(result) {
                angular.forEach(result.data, function (d) {
                    $scope.projets.push(d);
                });
            }, function errorCallback(response) {
            });

            $scope.load = function (id) {
                HemicyclePlan.get({id: id}, function (result) {
                    $scope.hemicyclePlan = result;
                });
            };

            var onSaveSuccess = function (result) {
                $scope.$emit('openassembleeApp:hemicyclePlanUpdate', result);
                $modalInstance.close(result);
                $scope.isSaving = false;
            };

            var onSaveError = function (result) {
                $scope.isSaving = false;
            };

            $scope.save = function () {
                $scope.isSaving = true;
                if ($scope.hemicyclePlan.id == null) {
                    var hemicyclePlanCreationDTO = {
                        label: $scope.hemicyclePlan.label,
                        configurationId: $scope.hemicyclePlan.configuration.id
                    };
                    if ($scope.properties.fromAlphabeticOrder) {
                        hemicyclePlanCreationDTO.fromAlphabeticOrder = true;
                    } else if ($scope.properties.fromSeance) {
                        hemicyclePlanCreationDTO.fromSeanceId = $scope.properties.fromSeance.id;
                    } else if ($scope.properties.fromProjet) {
                        hemicyclePlanCreationDTO.fromProjetId = $scope.properties.fromProjet.id;
                    }
                    HemicyclePlan.save(hemicyclePlanCreationDTO, onSaveSuccess, onSaveError);
                }
            };

            $scope.clear = function () {
                $modalInstance.dismiss('cancel');
            };
        }]);
