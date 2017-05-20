'use strict';

angular.module('openassembleeApp')
    .factory('AppartenanceCommissionPermanenteSearch', function ($resource) {
        return $resource('api/_search/appartenanceCommissionPermanentes/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
