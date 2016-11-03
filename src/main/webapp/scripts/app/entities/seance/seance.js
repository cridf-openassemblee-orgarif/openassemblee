'use strict';

angular.module('babylone14166App')
    .config(function ($stateProvider) {
        $stateProvider
            .state('seance', {
                parent: 'entity',
                url: '/seances',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Seances'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/seance/seances.html',
                        controller: 'SeanceController'
                    }
                },
                resolve: {
                }
            })
            .state('seance.detail', {
                parent: 'entity',
                url: '/seance/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Seance'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/seance/seance-detail.html',
                        controller: 'SeanceDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Seance', function($stateParams, Seance) {
                        return Seance.getDto({id : $stateParams.id});
                    }]
                }
            })
            .state('seance.new', {
                parent: 'seance',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/seance/seance-dialog.html',
                        controller: 'SeanceDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    intitule: null,
                                    type: null,
                                    date: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('seance', null, { reload: true });
                    }, function() {
                        $state.go('seance');
                    })
                }]
            })
            .state('seance.edit', {
                parent: 'seance',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/seance/seance-dialog.html',
                        controller: 'SeanceDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Seance', function(Seance) {
                                return Seance.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('seance', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('seance.delete', {
                parent: 'seance',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/seance/seance-delete-dialog.html',
                        controller: 'SeanceDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Seance', function(Seance) {
                                return Seance.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('seance', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
