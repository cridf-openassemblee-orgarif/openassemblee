'use strict';

angular.module('openassembleeApp')
    .factory('CommissionThematiqueSearch', function ($resource) {
        return $resource('api/_search/commissionThematiques/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
