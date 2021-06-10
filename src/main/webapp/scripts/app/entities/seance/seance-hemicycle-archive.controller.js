"use strict";

angular
    .module("openassembleeApp")
    .controller(
        "SeanceHemicycleArchiveController",
        function ($scope, $rootScope, $stateParams, entity, Seance) {
            window.loadHemicycleArchive({ archiveId: $stateParams.archiveId });
        }
    );
