'use strict';

angular.module('openassembleeApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('reunionCao', {
                parent: 'entity',
                url: '/reunionCaos',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ReunionCaos'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/reunionCao/reunionCaos.html',
                        controller: 'ReunionCaoController'
                    }
                },
                resolve: {
                }
            })
            .state('reunionCao.detail', {
                parent: 'entity',
                url: '/reunionCao/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ReunionCao'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/reunionCao/reunionCao-detail.html',
                        controller: 'ReunionCaoDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'ReunionCao', function($stateParams, ReunionCao) {
                        return ReunionCao.get({id : $stateParams.id});
                    }]
                }
            })
            .state('reunionCao.new', {
                parent: 'reunionCao',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/reunionCao/reunionCao-dialog.html',
                        controller: 'ReunionCaoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    libelle: null,
                                    date: null,
                                    heureDebut: null,
                                    heureFin: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('reunionCao', null, { reload: true });
                    }, function() {
                        $state.go('reunionCao');
                    })
                }]
            })
            .state('reunionCao.edit', {
                parent: 'reunionCao',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/reunionCao/reunionCao-dialog.html',
                        controller: 'ReunionCaoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ReunionCao', function(ReunionCao) {
                                return ReunionCao.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('reunionCao', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('reunionCao.delete', {
                parent: 'reunionCao',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/reunionCao/reunionCao-delete-dialog.html',
                        controller: 'ReunionCaoDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['ReunionCao', function(ReunionCao) {
                                return ReunionCao.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('reunionCao', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
