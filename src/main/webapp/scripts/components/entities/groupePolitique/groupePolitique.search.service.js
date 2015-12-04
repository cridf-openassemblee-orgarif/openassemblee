'use strict';

angular.module('babylone14166App')
    .factory('GroupePolitiqueSearch', function ($resource) {
        return $resource('api/_search/groupePolitiques/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
