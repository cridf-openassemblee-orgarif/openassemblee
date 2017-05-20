'use strict';

angular.module('openassembleeApp')
    .factory('FonctionGroupePolitiqueSearch', function ($resource) {
        return $resource('api/_search/fonctionGroupePolitiques/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
