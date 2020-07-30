'use strict';

angular.module('openassembleeApp')
    .factory('HemicyclePlan', function ($resource, DateUtils) {
        return $resource('api/hemicyclePlans/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.creationDate = DateUtils.convertDateTimeFromServer(data.creationDate);
                    data.lastModificationDate = DateUtils.convertDateTimeFromServer(data.lastModificationDate);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
