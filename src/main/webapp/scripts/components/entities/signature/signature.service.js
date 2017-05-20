'use strict';

angular.module('openassembleeApp')
    .factory('Signature', function ($resource, DateUtils) {
        return $resource('api/signatures/:id', {}, {
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
