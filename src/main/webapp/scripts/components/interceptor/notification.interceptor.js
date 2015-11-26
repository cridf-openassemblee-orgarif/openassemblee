 'use strict';

angular.module('babylone14166App')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-babylone14166App-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-babylone14166App-params')});
                }
                return response;
            }
        };
    });
