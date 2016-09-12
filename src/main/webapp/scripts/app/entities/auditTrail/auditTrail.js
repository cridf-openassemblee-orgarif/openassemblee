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
            })
            .state('auditTrail.new', {
                parent: 'auditTrail',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/auditTrail/auditTrail-dialog.html',
                        controller: 'AuditTrailDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    entity: null,
                                    entityId: null,
                                    parentEntity: null,
                                    parentEntityId: null,
                                    action: null,
                                    user: null,
                                    date: null,
                                    details: null,
                                    reason: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('auditTrail', null, { reload: true });
                    }, function() {
                        $state.go('auditTrail');
                    })
                }]
            })
            .state('auditTrail.edit', {
                parent: 'auditTrail',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/auditTrail/auditTrail-dialog.html',
                        controller: 'AuditTrailDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['AuditTrail', function(AuditTrail) {
                                return AuditTrail.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('auditTrail', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('auditTrail.delete', {
                parent: 'auditTrail',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/auditTrail/auditTrail-delete-dialog.html',
                        controller: 'AuditTrailDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['AuditTrail', function(AuditTrail) {
                                return AuditTrail.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('auditTrail', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
