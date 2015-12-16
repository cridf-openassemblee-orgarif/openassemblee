'use strict';

angular.module('babylone14166App')
    .factory('CommissionThematiqueSearch', function ($resource) {
        return $resource('api/_search/commissionThematiques/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
