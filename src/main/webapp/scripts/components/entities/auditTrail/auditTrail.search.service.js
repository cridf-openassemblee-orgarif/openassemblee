'use strict';

angular.module('babylone14166App')
    .factory('AuditTrailSearch', function ($resource) {
        return $resource('api/_search/auditTrails/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
