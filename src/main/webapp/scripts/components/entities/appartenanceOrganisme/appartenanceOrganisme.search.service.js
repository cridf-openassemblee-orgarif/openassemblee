'use strict';

angular.module('babylone14166App')
    .factory('AppartenanceOrganismeSearch', function ($resource) {
        return $resource('api/_search/appartenanceOrganismes/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
