'use strict';

angular.module('babylone14166App')
    .factory('ReunionCaoSearch', function ($resource) {
        return $resource('api/_search/reunionCaos/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
