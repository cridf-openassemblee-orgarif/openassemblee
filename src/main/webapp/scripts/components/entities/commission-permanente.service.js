'use strict';

angular.module('openassembleeApp')
    .factory('CommissionPermanente', function ($resource, DateUtils) {
        return $resource('api/commission-permanente', {}, {
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'getExecutif': {
                method: 'GET',
                url: 'api/executif',
                transformRequest: function (data) {
                    return angular.toJson(data);
                }
            },
        });
    });
