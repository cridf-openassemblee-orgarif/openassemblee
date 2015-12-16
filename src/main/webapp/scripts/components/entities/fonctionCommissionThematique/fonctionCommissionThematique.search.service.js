'use strict';

angular.module('babylone14166App')
    .factory('FonctionCommissionThematiqueSearch', function ($resource) {
        return $resource('api/_search/fonctionCommissionThematiques/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
