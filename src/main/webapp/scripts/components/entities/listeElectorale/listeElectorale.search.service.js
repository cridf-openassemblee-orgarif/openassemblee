'use strict';

angular.module('openassembleeApp')
    .factory('ListeElectoraleSearch', function ($resource) {
        return $resource('api/_search/listeElectorales/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
