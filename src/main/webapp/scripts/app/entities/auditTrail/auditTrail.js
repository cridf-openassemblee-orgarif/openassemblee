'use strict';

angular.module('babylone14166App')
    .config(function ($stateProvider) {
        $stateProvider
            .state('auditTrail', {
                parent: 'entity',
                url: '/auditTrails',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'AuditTrails'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/auditTrail/auditTrails.html',
                        controller: 'AuditTrailController'
                    }
                },
                resolve: {
                }
            })
            .state('auditTrail.detail', {
                parent: 'entity',
                url: '/auditTrail/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'AuditTrail'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/auditTrail/auditTrail-detail.html',
                        controller: 'AuditTrailDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'AuditTrail', function($stateParams, AuditTrail) {
                        return AuditTrail.get({id : $stateParams.id});
                    }]
                }
            });
    });
