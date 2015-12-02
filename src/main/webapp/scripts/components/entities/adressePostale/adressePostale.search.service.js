'use strict';

angular.module('babylone14166App')
    .factory('AdressePostaleSearch', function ($resource) {
        return $resource('api/_search/adressePostales/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
