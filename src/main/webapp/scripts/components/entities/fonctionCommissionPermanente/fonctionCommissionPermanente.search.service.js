'use strict';

angular.module('babylone14166App')
    .factory('FonctionCommissionPermanenteSearch', function ($resource) {
        return $resource('api/_search/fonctionCommissionPermanentes/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
