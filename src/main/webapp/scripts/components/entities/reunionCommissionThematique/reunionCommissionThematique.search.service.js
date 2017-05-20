'use strict';

angular.module('openassembleeApp')
    .factory('ReunionCommissionThematiqueSearch', function ($resource) {
        return $resource('api/_search/reunionCommissionThematiques/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
