'use strict';

angular.module('openassembleeApp')
    .factory('AppartenanceCommissionThematiqueSearch', function ($resource) {
        return $resource('api/_search/appartenanceCommissionThematiques/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
