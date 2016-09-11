'use strict';

angular.module('babylone14166App')
    .config(function ($stateProvider) {
        $stateProvider
            .state('pouvoir', {
                parent: 'entity',
                url: '/pouvoirs',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Pouvoirs'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/pouvoir/pouvoirs.html',
                        controller: 'PouvoirController'
                    }
                },
                resolve: {
                }
            })
            .state('pouvoir.detail', {
                parent: 'entity',
                url: '/pouvoir/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Pouvoir'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/pouvoir/pouvoir-detail.html',
                        controller: 'PouvoirDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Pouvoir', function($stateParams, Pouvoir) {
                        return Pouvoir.get({id : $stateParams.id});
                    }]
                }
            })
            .state('pouvoir.new', {
                parent: 'pouvoir',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/pouvoir/pouvoir-dialog.html',
                        controller: 'PouvoirDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    dateDebut: null,
                                    heureDebut: null,
                                    dateFin: null,
                                    heureFin: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('pouvoir', null, { reload: true });
                    }, function() {
                        $state.go('pouvoir');
                    })
                }]
            })
            .state('pouvoir.edit', {
                parent: 'pouvoir',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/pouvoir/pouvoir-dialog.html',
                        controller: 'PouvoirDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Pouvoir', function(Pouvoir) {
                                return Pouvoir.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('pouvoir', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('pouvoir.delete', {
                parent: 'pouvoir',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/pouvoir/pouvoir-delete-dialog.html',
                        controller: 'PouvoirDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Pouvoir', function(Pouvoir) {
                                return Pouvoir.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('pouvoir', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
