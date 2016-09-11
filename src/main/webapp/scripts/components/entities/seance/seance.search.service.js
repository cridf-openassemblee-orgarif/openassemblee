'use strict';

angular.module('babylone14166App')
    .factory('SeanceSearch', function ($resource) {
        return $resource('api/_search/seances/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
