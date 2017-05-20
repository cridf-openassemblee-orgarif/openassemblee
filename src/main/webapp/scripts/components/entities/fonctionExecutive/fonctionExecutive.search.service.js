'use strict';

angular.module('openassembleeApp')
    .factory('FonctionExecutiveSearch', function ($resource) {
        return $resource('api/_search/fonctionExecutives/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
