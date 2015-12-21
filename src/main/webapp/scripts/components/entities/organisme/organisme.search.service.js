'use strict';

angular.module('babylone14166App')
    .factory('OrganismeSearch', function ($resource) {
        return $resource('api/_search/organismes/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
