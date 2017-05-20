'use strict';

angular.module('openassembleeApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('reunionCommissionThematique', {
                parent: 'entity',
                url: '/reunionCommissionThematiques',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ReunionCommissionThematiques'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/reunionCommissionThematique/reunionCommissionThematiques.html',
                        controller: 'ReunionCommissionThematiqueController'
                    }
                },
                resolve: {
                }
            })
            .state('reunionCommissionThematique.detail', {
                parent: 'entity',
                url: '/reunionCommissionThematique/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ReunionCommissionThematique'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/reunionCommissionThematique/reunionCommissionThematique-detail.html',
                        controller: 'ReunionCommissionThematiqueDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'ReunionCommissionThematique', function($stateParams, ReunionCommissionThematique) {
                        return ReunionCommissionThematique.get({id : $stateParams.id});
                    }]
                }
            })
            .state('reunionCommissionThematique.new', {
                parent: 'reunionCommissionThematique',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/reunionCommissionThematique/reunionCommissionThematique-dialog.html',
                        controller: 'ReunionCommissionThematiqueDialogController',
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
                        $state.go('reunionCommissionThematique', null, { reload: true });
                    }, function() {
                        $state.go('reunionCommissionThematique');
                    })
                }]
            })
            .state('reunionCommissionThematique.edit', {
                parent: 'reunionCommissionThematique',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/reunionCommissionThematique/reunionCommissionThematique-dialog.html',
                        controller: 'ReunionCommissionThematiqueDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ReunionCommissionThematique', function(ReunionCommissionThematique) {
                                return ReunionCommissionThematique.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('reunionCommissionThematique', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('reunionCommissionThematique.delete', {
                parent: 'reunionCommissionThematique',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/reunionCommissionThematique/reunionCommissionThematique-delete-dialog.html',
                        controller: 'ReunionCommissionThematiqueDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['ReunionCommissionThematique', function(ReunionCommissionThematique) {
                                return ReunionCommissionThematique.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('reunionCommissionThematique', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
