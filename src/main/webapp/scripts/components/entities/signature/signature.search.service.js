'use strict';

angular.module('openassembleeApp')
    .factory('SignatureSearch', function ($resource) {
        return $resource('api/_search/signatures/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
