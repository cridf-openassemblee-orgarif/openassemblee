'use strict';

angular.module('babylone14166App')
    .config(function ($stateProvider) {
        $stateProvider
            .state('organisme', {
                parent: 'entity',
                url: '/organismes',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Organismes'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/organisme/organismes.html',
                        controller: 'OrganismeController'
                    }
                },
                resolve: {
                }
            })
            .state('organisme.detail', {
                parent: 'entity',
                url: '/organisme/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Organisme'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/organisme/organisme-detail.html',
                        controller: 'OrganismeDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Organisme', function($stateParams, Organisme) {
                        return Organisme.get({id : $stateParams.id});
                    }]
                }
            })
            .state('organisme.new', {
                parent: 'organisme',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/organisme/organisme-dialog.html',
                        controller: 'OrganismeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    nom: null,
                                    codeRNE: null,
                                    siret: null,
                                    sigle: null,
                                    type: null,
                                    secteur: null,
                                    dateDebut: null,
                                    dateFin: null,
                                    motifFin: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('organisme', null, { reload: true });
                    }, function() {
                        $state.go('organisme');
                    })
                }]
            })
            .state('organisme.edit', {
                parent: 'organisme',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/organisme/organisme-dialog.html',
                        controller: 'OrganismeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Organisme', function(Organisme) {
                                return Organisme.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('organisme', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('organisme.delete', {
                parent: 'organisme',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/organisme/organisme-delete-dialog.html',
                        controller: 'OrganismeDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Organisme', function(Organisme) {
                                return Organisme.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('organisme', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
