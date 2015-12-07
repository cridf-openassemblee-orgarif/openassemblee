'use strict';

angular.module('babylone14166App')
    .config(function ($stateProvider) {
        $stateProvider
            .state('appartenanceCommissionPermanente', {
                parent: 'entity',
                url: '/appartenanceCommissionPermanentes',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'AppartenanceCommissionPermanentes'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/appartenanceCommissionPermanente/appartenanceCommissionPermanentes.html',
                        controller: 'AppartenanceCommissionPermanenteController'
                    }
                },
                resolve: {
                }
            })
            .state('appartenanceCommissionPermanente.detail', {
                parent: 'entity',
                url: '/appartenanceCommissionPermanente/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'AppartenanceCommissionPermanente'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/appartenanceCommissionPermanente/appartenanceCommissionPermanente-detail.html',
                        controller: 'AppartenanceCommissionPermanenteDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'AppartenanceCommissionPermanente', function($stateParams, AppartenanceCommissionPermanente) {
                        return AppartenanceCommissionPermanente.get({id : $stateParams.id});
                    }]
                }
            })
            .state('appartenanceCommissionPermanente.new', {
                parent: 'appartenanceCommissionPermanente',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/appartenanceCommissionPermanente/appartenanceCommissionPermanente-dialog.html',
                        controller: 'AppartenanceCommissionPermanenteDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('appartenanceCommissionPermanente', null, { reload: true });
                    }, function() {
                        $state.go('appartenanceCommissionPermanente');
                    })
                }]
            })
            .state('appartenanceCommissionPermanente.edit', {
                parent: 'appartenanceCommissionPermanente',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/appartenanceCommissionPermanente/appartenanceCommissionPermanente-dialog.html',
                        controller: 'AppartenanceCommissionPermanenteDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['AppartenanceCommissionPermanente', function(AppartenanceCommissionPermanente) {
                                return AppartenanceCommissionPermanente.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('appartenanceCommissionPermanente', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('appartenanceCommissionPermanente.delete', {
                parent: 'appartenanceCommissionPermanente',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/appartenanceCommissionPermanente/appartenanceCommissionPermanente-delete-dialog.html',
                        controller: 'AppartenanceCommissionPermanenteDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['AppartenanceCommissionPermanente', function(AppartenanceCommissionPermanente) {
                                return AppartenanceCommissionPermanente.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('appartenanceCommissionPermanente', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
