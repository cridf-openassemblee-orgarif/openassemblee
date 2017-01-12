'use strict';

angular.module('babylone14166App')
    .config(function ($stateProvider) {
        $stateProvider
            .state('autreMandat', {
                parent: 'entity',
                url: '/autreMandats',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'AutreMandats'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/autreMandat/autreMandats.html',
                        controller: 'AutreMandatController'
                    }
                },
                resolve: {
                }
            })
            .state('autreMandat.detail', {
                parent: 'entity',
                url: '/autreMandat/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'AutreMandat'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/autreMandat/autreMandat-detail.html',
                        controller: 'AutreMandatDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'AutreMandat', function($stateParams, AutreMandat) {
                        return AutreMandat.get({id : $stateParams.id});
                    }]
                }
            })
            .state('autreMandat.new', {
                parent: 'autreMandat',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/autreMandat/autreMandat-dialog.html',
                        controller: 'AutreMandatDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    collectiviteOuOrganisme: null,
                                    fonction: null,
                                    dateDebut: null,
                                    dateFin: null,
                                    motifFin: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('autreMandat', null, { reload: true });
                    }, function() {
                        $state.go('autreMandat');
                    })
                }]
            })
            .state('autreMandat.edit', {
                parent: 'autreMandat',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/autreMandat/autreMandat-dialog.html',
                        controller: 'AutreMandatDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['AutreMandat', function(AutreMandat) {
                                return AutreMandat.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('autreMandat', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('autreMandat.delete', {
                parent: 'autreMandat',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/autreMandat/autreMandat-delete-dialog.html',
                        controller: 'AutreMandatDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['AutreMandat', function(AutreMandat) {
                                return AutreMandat.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('autreMandat', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
