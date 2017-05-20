'use strict';

angular.module('openassembleeApp')
    .factory('GroupePolitiqueSearch', function ($resource) {
        return $resource('api/_search/groupePolitiques/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
