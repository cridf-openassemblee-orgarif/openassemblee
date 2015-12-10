'use strict';

angular.module('babylone14166App')
    .factory('CommissionPermanente', function ($resource, DateUtils) {
        return $resource('api/commission-permanente', {}, {
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    //data.dateNaissance = DateUtils.convertLocaleDateFromServer(data.dateNaissance);
                    return data;
                }
            },
        });
    });
