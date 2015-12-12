'use strict';

angular.module('babylone14166App')
    .factory('AppartenanceGroupePolitiqueSearch', function ($resource) {
        return $resource('api/_search/appartenanceGroupePolitiques/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
