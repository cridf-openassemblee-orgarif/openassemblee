'use strict';

angular.module('openassembleeApp')
    .factory('FonctionCommissionPermanenteSearch', function ($resource) {
        return $resource('api/_search/fonctionCommissionPermanentes/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
