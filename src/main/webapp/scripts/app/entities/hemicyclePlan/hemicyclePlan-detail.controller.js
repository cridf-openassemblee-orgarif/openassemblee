"use strict";

angular
    .module("openassembleeApp")
    .controller(
        "HemicyclePlanDetailController",
        function (
            $scope,
            $rootScope,
            $stateParams,
            entity,
            HemicyclePlan,
            HemicycleConfiguration,
            Seance
        ) {
            window.loadHemicycle({
                planId: $stateParams.id,
                isProjet: true,
            });
        }
    );
