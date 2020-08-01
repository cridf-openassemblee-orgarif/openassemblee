'use strict';

angular.module('openassembleeApp')
    .factory('HemicycleArchiveSearch', function ($resource) {
        return $resource('api/_search/hemicycleArchives/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
