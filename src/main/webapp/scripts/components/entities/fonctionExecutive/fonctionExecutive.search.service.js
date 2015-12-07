'use strict';

angular.module('babylone14166App')
    .factory('FonctionExecutiveSearch', function ($resource) {
        return $resource('api/_search/fonctionExecutives/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
