'use strict';

angular.module('babylone14166App')
    .factory('AppartenanceCommissionPermanenteSearch', function ($resource) {
        return $resource('api/_search/appartenanceCommissionPermanentes/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
