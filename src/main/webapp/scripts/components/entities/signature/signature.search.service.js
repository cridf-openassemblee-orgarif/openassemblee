'use strict';

angular.module('babylone14166App')
    .factory('SignatureSearch', function ($resource) {
        return $resource('api/_search/signatures/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
