'use strict';

angular.module('babylone14166App')
    .factory('CommissionPermanente', function ($resource, DateUtils) {
        return $resource('api/commission-permanente', {}, {
            'addElu': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dateDebut = DateUtils.convertLocaleDateToServer(data.dateDebut);
                    return angular.toJson(data);
                }
            }
        });
    });
