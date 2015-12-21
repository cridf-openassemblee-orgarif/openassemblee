'use strict';

angular.module('babylone14166App')
    .factory('AppartenanceOrganisme', function ($resource, DateUtils) {
        return $resource('api/appartenanceOrganismes/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dateDebut = DateUtils.convertLocaleDateFromServer(data.dateDebut);
                    data.dateFin = DateUtils.convertLocaleDateFromServer(data.dateFin);
                    data.dateNomination = DateUtils.convertLocaleDateFromServer(data.dateNomination);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dateDebut = DateUtils.convertLocaleDateToServer(data.dateDebut);
                    data.dateFin = DateUtils.convertLocaleDateToServer(data.dateFin);
                    data.dateNomination = DateUtils.convertLocaleDateToServer(data.dateNomination);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dateDebut = DateUtils.convertLocaleDateToServer(data.dateDebut);
                    data.dateFin = DateUtils.convertLocaleDateToServer(data.dateFin);
                    data.dateNomination = DateUtils.convertLocaleDateToServer(data.dateNomination);
                    return angular.toJson(data);
                }
            }
        });
    });
