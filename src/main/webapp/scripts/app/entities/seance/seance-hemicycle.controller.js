'use strict';

angular.module('openassembleeApp')
    .controller('SeanceHemicycleController', function ($scope, $rootScope, $stateParams, entity, Seance) {
            window.loadHemicycle({seanceId: $stateParams.id});
    });
