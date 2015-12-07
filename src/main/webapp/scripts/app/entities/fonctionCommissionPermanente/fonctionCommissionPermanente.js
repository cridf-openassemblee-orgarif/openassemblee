'use strict';

angular.module('babylone14166App')
    .config(function ($stateProvider) {
        $stateProvider
            .state('fonctionCommissionPermanente', {
                parent: 'entity',
                url: '/fonctionCommissionPermanentes',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'FonctionCommissionPermanentes'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/fonctionCommissionPermanente/fonctionCommissionPermanentes.html',
                        controller: 'FonctionCommissionPermanenteController'
                    }
                },
                resolve: {
                }
            })
            .state('fonctionCommissionPermanente.detail', {
                parent: 'entity',
                url: '/fonctionCommissionPermanente/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'FonctionCommissionPermanente'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/fonctionCommissionPermanente/fonctionCommissionPermanente-detail.html',
                        controller: 'FonctionCommissionPermanenteDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'FonctionCommissionPermanente', function($stateParams, FonctionCommissionPermanente) {
                        return FonctionCommissionPermanente.get({id : $stateParams.id});
                    }]
                }
            })
            .state('fonctionCommissionPermanente.new', {
                parent: 'fonctionCommissionPermanente',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/fonctionCommissionPermanente/fonctionCommissionPermanente-dialog.html',
                        controller: 'FonctionCommissionPermanenteDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    fonction: null,
                                    dateDebut: null,
                                    dateFin: null,
                                    motifFin: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('fonctionCommissionPermanente', null, { reload: true });
                    }, function() {
                        $state.go('fonctionCommissionPermanente');
                    })
                }]
            })
            .state('fonctionCommissionPermanente.edit', {
                parent: 'fonctionCommissionPermanente',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/fonctionCommissionPermanente/fonctionCommissionPermanente-dialog.html',
                        controller: 'FonctionCommissionPermanenteDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['FonctionCommissionPermanente', function(FonctionCommissionPermanente) {
                                return FonctionCommissionPermanente.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('fonctionCommissionPermanente', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('fonctionCommissionPermanente.delete', {
                parent: 'fonctionCommissionPermanente',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/fonctionCommissionPermanente/fonctionCommissionPermanente-delete-dialog.html',
                        controller: 'FonctionCommissionPermanenteDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['FonctionCommissionPermanente', function(FonctionCommissionPermanente) {
                                return FonctionCommissionPermanente.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('fonctionCommissionPermanente', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
