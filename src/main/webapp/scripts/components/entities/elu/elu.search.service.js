'use strict';

angular.module('babylone14166App')
    .factory('EluSearch', function ($resource) {
        return $resource('api/_search/elus/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
