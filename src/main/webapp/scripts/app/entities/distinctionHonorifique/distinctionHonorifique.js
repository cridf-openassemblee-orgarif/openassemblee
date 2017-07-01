'use strict';

angular.module('openassembleeApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('distinctionHonorifique', {
                parent: 'entity',
                url: '/distinctionHonorifiques',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'DistinctionHonorifiques'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/distinctionHonorifique/distinctionHonorifiques.html',
                        controller: 'DistinctionHonorifiqueController'
                    }
                },
                resolve: {
                }
            })
            .state('distinctionHonorifique.detail', {
                parent: 'entity',
                url: '/distinctionHonorifique/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'DistinctionHonorifique'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/distinctionHonorifique/distinctionHonorifique-detail.html',
                        controller: 'DistinctionHonorifiqueDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'DistinctionHonorifique', function($stateParams, DistinctionHonorifique) {
                        return DistinctionHonorifique.get({id : $stateParams.id});
                    }]
                }
            })
            .state('distinctionHonorifique.new', {
                parent: 'distinctionHonorifique',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/distinctionHonorifique/distinctionHonorifique-dialog.html',
                        controller: 'DistinctionHonorifiqueDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    titre: null,
                                    date: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('distinctionHonorifique', null, { reload: true });
                    }, function() {
                        $state.go('distinctionHonorifique');
                    })
                }]
            })
            .state('distinctionHonorifique.edit', {
                parent: 'distinctionHonorifique',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/distinctionHonorifique/distinctionHonorifique-dialog.html',
                        controller: 'DistinctionHonorifiqueDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['DistinctionHonorifique', function(DistinctionHonorifique) {
                                return DistinctionHonorifique.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('distinctionHonorifique', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('distinctionHonorifique.delete', {
                parent: 'distinctionHonorifique',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/distinctionHonorifique/distinctionHonorifique-delete-dialog.html',
                        controller: 'DistinctionHonorifiqueDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['DistinctionHonorifique', function(DistinctionHonorifique) {
                                return DistinctionHonorifique.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('distinctionHonorifique', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
