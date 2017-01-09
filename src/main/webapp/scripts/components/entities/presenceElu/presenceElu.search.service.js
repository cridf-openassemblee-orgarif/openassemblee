'use strict';

angular.module('babylone14166App')
    .factory('PresenceEluSearch', function ($resource) {
        return $resource('api/_search/presenceElus/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
