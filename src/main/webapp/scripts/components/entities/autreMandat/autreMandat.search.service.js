'use strict';

angular.module('babylone14166App')
    .factory('AutreMandatSearch', function ($resource) {
        return $resource('api/_search/autreMandats/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
