'use strict';

angular.module('openassembleeApp')
    .factory('PresenceEluSearch', function ($resource) {
        return $resource('api/_search/presenceElus/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
