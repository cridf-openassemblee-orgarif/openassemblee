'use strict';

angular.module('babylone14166App')
    .factory('AppartenanceCommissionThematiqueSearch', function ($resource) {
        return $resource('api/_search/appartenanceCommissionThematiques/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
