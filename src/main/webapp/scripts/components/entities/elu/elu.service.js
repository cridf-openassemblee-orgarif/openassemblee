'use strict';

angular.module('babylone14166App')
    .factory('Elu', function ($resource, DateUtils) {
        return $resource('api/elus/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dateNaissance = DateUtils.convertLocaleDateFromServer(data.dateNaissance);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dateNaissance = DateUtils.convertLocaleDateToServer(data.dateNaissance);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dateNaissance = DateUtils.convertLocaleDateToServer(data.dateNaissance);
                    return angular.toJson(data);
                }
            },
            'saveAdressePostale': {
                method: 'POST',
                url: 'api/elus/:id/adressePostale',
                transformRequest: function (data) {
                    return angular.toJson(data);
                }
            },
            'updateAdressePostale': {
                method: 'PUT',
                url: 'api/elus/:id/adressePostale',
                transformRequest: function (data) {
                    return angular.toJson(data);
                }
            },
            'deleteAdressePostale': {
                method: 'DELETE',
                url: 'api/elus/:eluId/adressePostale/:adressePostaleId',
                transformRequest: function (data) {
                    return angular.toJson(data);
                }
            },
            'saveAdresseMail': {
                method: 'POST',
                url: 'api/elus/:id/adresseMail',
                transformRequest: function (data) {
                    return angular.toJson(data);
                }
            },
            'saveIdentiteInternet': {
                method: 'POST',
                url: 'api/elus/:id/identiteInternet',
                transformRequest: function (data) {
                    return angular.toJson(data);
                }
            },
            'saveNumeroFax': {
                method: 'POST',
                url: 'api/elus/:id/numeroFax',
                transformRequest: function (data) {
                    return angular.toJson(data);
                }
            },
            'saveNumeroTelephone': {
                method: 'POST',
                url: 'api/elus/:id/numeroTelephone',
                transformRequest: function (data) {
                    return angular.toJson(data);
                }
            }
        });
    });
