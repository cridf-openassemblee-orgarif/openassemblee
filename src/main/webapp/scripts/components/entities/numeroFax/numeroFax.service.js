'use strict';

angular.module('babylone14166App')
    .factory('NumeroFax', function ($resource, DateUtils) {
        return $resource('api/numeroFaxs/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
