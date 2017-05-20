'use strict';

angular.module('openassembleeApp')
    .factory('FonctionCommissionThematiqueSearch', function ($resource) {
        return $resource('api/_search/fonctionCommissionThematiques/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
