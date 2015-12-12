'use strict';

angular.module('babylone14166App')
    .factory('FonctionGroupePolitiqueSearch', function ($resource) {
        return $resource('api/_search/fonctionGroupePolitiques/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
