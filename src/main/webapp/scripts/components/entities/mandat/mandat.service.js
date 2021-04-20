'use strict';

angular.module('openassembleeApp')
    .factory('Mandat', function ($resource, DateUtils) {
        return $resource('api/mandats/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dateDebut = DateUtils.convertLocaleDateFromServer(data.dateDebut);
                    data.dateDemission = DateUtils.convertLocaleDateFromServer(data.dateDemission);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dateDebut = DateUtils.convertLocaleDateToServer(data.dateDebut);
                    data.dateDemission = DateUtils.convertLocaleDateToServer(data.dateDemission);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dateDebut = DateUtils.convertLocaleDateToServer(data.dateDebut);
                    data.dateDemission = DateUtils.convertLocaleDateToServer(data.dateDemission);
                    return angular.toJson(data);
                }
            }
        });
    });
