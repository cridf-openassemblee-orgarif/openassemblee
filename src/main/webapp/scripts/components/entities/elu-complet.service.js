'use strict';

angular.module('babylone14166App')
    .factory('EluComplet', function ($resource, DateUtils) {
        return $resource('api/elu-complet/:id', {}, {
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    if (data.elu.dateNaissance != undefined) {
                        data.elu.dateNaissance = DateUtils.convertLocaleDateFromServer(data.elu.dateNaissance);
                    }
                    return data;
                }
            },
        });
    });
