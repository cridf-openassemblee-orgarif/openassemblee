'use strict';

angular.module('openassembleeApp')
    .factory('Pouvoir', function ($resource, DateUtils) {
        return $resource('api/pouvoirs/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dateDebut = DateUtils.convertLocaleDateFromServer(data.dateDebut);
                    data.dateFin = DateUtils.convertLocaleDateFromServer(data.dateFin);
                    return data;
                }
            },
            'getAllOpen': {
                method: 'GET',
                url: 'api/pouvoirs/open/:seanceId',
                isArray: true,
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.map(function (p) {
                        p.dateDebut = DateUtils.convertLocaleDateFromServer(p.dateDebut);
                        p.dateFin = DateUtils.convertLocaleDateFromServer(p.dateFin);
                        return p;
                    });
                    return data;
                }
            },
            'closeAll': {
                method: 'POST',
                url: 'api/pouvoirs/close-all/:seanceId',
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dateDebut = DateUtils.convertLocaleDateToServer(data.dateDebut);
                    data.dateFin = DateUtils.convertLocaleDateToServer(data.dateFin);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dateDebut = DateUtils.convertLocaleDateToServer(data.dateDebut);
                    data.dateFin = DateUtils.convertLocaleDateToServer(data.dateFin);
                    return angular.toJson(data);
                }
            }
        });
    });
