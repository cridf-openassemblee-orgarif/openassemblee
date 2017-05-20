'use strict';

angular.module('openassembleeApp')
    .factory('ReunionCaoSearch', function ($resource) {
        return $resource('api/_search/reunionCaos/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
