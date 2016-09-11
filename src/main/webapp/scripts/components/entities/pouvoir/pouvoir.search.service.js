'use strict';

angular.module('babylone14166App')
    .factory('PouvoirSearch', function ($resource) {
        return $resource('api/_search/pouvoirs/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
