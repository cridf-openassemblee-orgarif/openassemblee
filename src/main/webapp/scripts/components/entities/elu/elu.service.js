"use strict";

angular
    .module("openassembleeApp")
    .factory("Elu", function ($resource, DateUtils) {
        return $resource(
            "api/elus/:id",
            {},
            {
                query: { method: "GET", isArray: true },
                get: {
                    method: "GET",
                    transformResponse: function (data) {
                        data = angular.fromJson(data);
                        data.dateNaissance =
                            DateUtils.convertLocaleDateFromServer(
                                data.dateNaissance
                            );
                        data.dateDemission =
                            DateUtils.convertLocaleDateFromServer(
                                data.dateDemission
                            );
                        return data;
                    },
                },
                update: {
                    method: "PUT",
                    transformRequest: function (data) {
                        data.dateNaissance =
                            DateUtils.convertLocaleDateToServer(
                                data.dateNaissance
                            );
                        data.dateDemission =
                            DateUtils.convertLocaleDateToServer(
                                data.dateDemission
                            );
                        return angular.toJson(data);
                    },
                },
                save: {
                    method: "POST",
                    transformRequest: function (data) {
                        data.dateNaissance =
                            DateUtils.convertLocaleDateToServer(
                                data.dateNaissance
                            );
                        data.dateDemission =
                            DateUtils.convertLocaleDateToServer(
                                data.dateDemission
                            );
                        return angular.toJson(data);
                    },
                },
                saveAdressePostale: {
                    method: "POST",
                    url: "api/elus/:id/adressePostale",
                    transformRequest: function (data) {
                        return angular.toJson(data);
                    },
                },
                updateAdressePostale: {
                    method: "PUT",
                    url: "api/elus/:id/adressePostale",
                    transformRequest: function (data) {
                        return angular.toJson(data);
                    },
                },
                deleteAdressePostale: {
                    method: "DELETE",
                    url: "api/elus/:eluId/adressePostale/:adressePostaleId",
                    transformRequest: function (data) {
                        return angular.toJson(data);
                    },
                },
                saveAdresseMail: {
                    method: "POST",
                    url: "api/elus/:id/adresseMail",
                    transformRequest: function (data) {
                        return angular.toJson(data);
                    },
                },
                updateAdresseMail: {
                    method: "PUT",
                    url: "api/elus/:id/adresseMail",
                    transformRequest: function (data) {
                        return angular.toJson(data);
                    },
                },
                deleteAdresseMail: {
                    method: "DELETE",
                    url: "api/elus/:eluId/adresseMail/:adresseMailId",
                    transformRequest: function (data) {
                        return angular.toJson(data);
                    },
                },
                saveIdentiteInternet: {
                    method: "POST",
                    url: "api/elus/:id/identiteInternet",
                    transformRequest: function (data) {
                        return angular.toJson(data);
                    },
                },
                updateIdentiteInternet: {
                    method: "PUT",
                    url: "api/elus/:id/identiteInternet",
                    transformRequest: function (data) {
                        return angular.toJson(data);
                    },
                },
                deleteIdentiteInternet: {
                    method: "DELETE",
                    url: "api/elus/:eluId/identiteInternet/:identiteInternetId",
                    transformRequest: function (data) {
                        return angular.toJson(data);
                    },
                },
                saveNumeroFax: {
                    method: "POST",
                    url: "api/elus/:id/numeroFax",
                    transformRequest: function (data) {
                        return angular.toJson(data);
                    },
                },
                updateNumeroFax: {
                    method: "PUT",
                    url: "api/elus/:id/numeroFax",
                    transformRequest: function (data) {
                        return angular.toJson(data);
                    },
                },
                deleteNumeroFax: {
                    method: "DELETE",
                    url: "api/elus/:eluId/numeroFax/:numeroFaxId",
                    transformRequest: function (data) {
                        return angular.toJson(data);
                    },
                },
                saveNumeroTelephone: {
                    method: "POST",
                    url: "api/elus/:id/numeroTelephone",
                    transformRequest: function (data) {
                        return angular.toJson(data);
                    },
                },
                updateNumeroTelephone: {
                    method: "PUT",
                    url: "api/elus/:id/numeroTelephone",
                    transformRequest: function (data) {
                        return angular.toJson(data);
                    },
                },
                deleteNumeroTelephone: {
                    method: "DELETE",
                    url: "api/elus/:eluId/numeroTelephone/:numeroTelephoneId",
                    transformRequest: function (data) {
                        return angular.toJson(data);
                    },
                },
            }
        );
    });
