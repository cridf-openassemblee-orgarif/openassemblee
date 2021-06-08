'use strict';

angular.module('openassembleeApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('listeElectorale', {
                parent: 'entity',
                url: '/listeElectorales',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ListeElectorales'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/listeElectorale/listeElectorales.html',
                        controller: 'ListeElectoraleController'
                    }
                },
                resolve: {
                }
            })
            .state('listeElectorale.detail', {
                parent: 'entity',
                url: '/listeElectorale/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ListeElectorale'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/listeElectorale/listeElectorale-detail.html',
                        controller: 'ListeElectoraleDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'ListeElectorale', function($stateParams, ListeElectorale) {
                        return ListeElectorale.get({id : $stateParams.id});
                    }]
                }
            })
            .state('listeElectorale.new', {
                parent: 'listeElectorale',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/listeElectorale/listeElectorale-dialog.html',
                        controller: 'ListeElectoraleDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    nom: null,
                                    nomCourt: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('listeElectorale', null, { reload: true });
                    }, function() {
                        $state.go('listeElectorale');
                    })
                }]
            })
            .state('listeElectorale.edit', {
                parent: 'listeElectorale',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/listeElectorale/listeElectorale-dialog.html',
                        controller: 'ListeElectoraleDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ListeElectorale', function(ListeElectorale) {
                                return ListeElectorale.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('listeElectorale', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('listeElectorale.delete', {
                parent: 'listeElectorale',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/listeElectorale/listeElectorale-delete-dialog.html',
                        controller: 'ListeElectoraleDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['ListeElectorale', function(ListeElectorale) {
                                return ListeElectorale.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('listeElectorale', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
