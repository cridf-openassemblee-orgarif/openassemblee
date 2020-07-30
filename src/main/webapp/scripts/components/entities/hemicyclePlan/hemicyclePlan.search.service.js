'use strict';

angular.module('openassembleeApp')
    .factory('HemicyclePlanSearch', function ($resource) {
        return $resource('api/_search/hemicyclePlans/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
