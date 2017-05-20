'use strict';

angular.module('openassembleeApp')
    .factory('AppartenanceOrganismeSearch', function ($resource) {
        return $resource('api/_search/appartenanceOrganismes/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
