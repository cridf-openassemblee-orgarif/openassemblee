'use strict';

angular.module('babylone14166App')
    .config(function ($stateProvider) {
        $stateProvider
            .state('fonctionExecutive', {
                parent: 'entity',
                url: '/fonctionExecutives',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'FonctionExecutives'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/fonctionExecutive/fonctionExecutives.html',
                        controller: 'FonctionExecutiveController'
                    }
                },
                resolve: {
                }
            })
            .state('fonctionExecutive.detail', {
                parent: 'entity',
                url: '/fonctionExecutive/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'FonctionExecutive'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/fonctionExecutive/fonctionExecutive-detail.html',
                        controller: 'FonctionExecutiveDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'FonctionExecutive', function($stateParams, FonctionExecutive) {
                        return FonctionExecutive.get({id : $stateParams.id});
                    }]
                }
            })
            .state('fonctionExecutive.new', {
                parent: 'fonctionExecutive',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/fonctionExecutive/fonctionExecutive-dialog.html',
                        controller: 'FonctionExecutiveDialogController',
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
                        $state.go('fonctionExecutive', null, { reload: true });
                    }, function() {
                        $state.go('fonctionExecutive');
                    })
                }]
            })
            .state('fonctionExecutive.edit', {
                parent: 'fonctionExecutive',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/fonctionExecutive/fonctionExecutive-dialog.html',
                        controller: 'FonctionExecutiveDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['FonctionExecutive', function(FonctionExecutive) {
                                return FonctionExecutive.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('fonctionExecutive', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('fonctionExecutive.delete', {
                parent: 'fonctionExecutive',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/fonctionExecutive/fonctionExecutive-delete-dialog.html',
                        controller: 'FonctionExecutiveDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['FonctionExecutive', function(FonctionExecutive) {
                                return FonctionExecutive.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('fonctionExecutive', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
