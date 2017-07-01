'use strict';

angular.module('openassembleeApp')
    .factory('DistinctionHonorifique', function ($resource, DateUtils) {
        return $resource('api/distinctionHonorifiques/:id', {}, {
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
