'use strict';

angular.module('babylone14166App')
    .config(function ($stateProvider) {
        $stateProvider
            .state('adressePostale', {
                parent: 'entity',
                url: '/adressePostales',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'AdressePostales'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/adressePostale/adressePostales.html',
                        controller: 'AdressePostaleController'
                    }
                },
                resolve: {
                }
            })
            .state('adressePostale.detail', {
                parent: 'entity',
                url: '/adressePostale/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'AdressePostale'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/adressePostale/adressePostale-detail.html',
                        controller: 'AdressePostaleDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'AdressePostale', function($stateParams, AdressePostale) {
                        return AdressePostale.get({id : $stateParams.id});
                    }]
                }
            })
            .state('adressePostale.new', {
                parent: 'adressePostale',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/adressePostale/adressePostale-dialog.html',
                        controller: 'AdressePostaleDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    natureProPerso: null,
                                    rue: null,
                                    codePostal: null,
                                    ville: null,
                                    niveauConfidentialite: null,
                                    adresseDeCorrespondance: null,
                                    publicationAnnuaire: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('adressePostale', null, { reload: true });
                    }, function() {
                        $state.go('adressePostale');
                    })
                }]
            })
            .state('adressePostale.edit', {
                parent: 'adressePostale',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/adressePostale/adressePostale-dialog.html',
                        controller: 'AdressePostaleDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['AdressePostale', function(AdressePostale) {
                                return AdressePostale.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('adressePostale', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('adressePostale.delete', {
                parent: 'adressePostale',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/adressePostale/adressePostale-delete-dialog.html',
                        controller: 'AdressePostaleDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['AdressePostale', function(AdressePostale) {
                                return AdressePostale.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('adressePostale', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
