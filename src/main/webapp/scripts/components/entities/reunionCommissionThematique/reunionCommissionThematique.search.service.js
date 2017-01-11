'use strict';

angular.module('babylone14166App')
    .factory('ReunionCommissionThematiqueSearch', function ($resource) {
        return $resource('api/_search/reunionCommissionThematiques/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
