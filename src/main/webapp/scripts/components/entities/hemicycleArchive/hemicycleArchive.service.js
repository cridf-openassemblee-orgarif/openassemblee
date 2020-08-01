'use strict';

angular.module('openassembleeApp')
    .factory('HemicycleArchive', function ($resource, DateUtils) {
        return $resource('api/hemicycleArchives/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.date = DateUtils.convertDateTimeFromServer(data.date);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
