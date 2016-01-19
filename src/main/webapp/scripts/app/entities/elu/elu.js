'use strict';

angular.module('babylone14166App')
    .config(function ($stateProvider) {
        $stateProvider
            .state('elu', {
                parent: 'entity',
                url: '/elus',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Elus'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/elu/elus.html',
                        controller: 'EluController'
                    }
                },
                resolve: {
                }
            })
            .state('elu.detail', {
                parent: 'entity',
                url: '/elu/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Elu'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/elu/elu-detail.html',
                        controller: 'EluDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Elu', function($stateParams, Elu) {
                        return Elu.get({id : $stateParams.id});
                    }]
                }
            })
            .state('elu.new', {
                parent: 'elu',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/elu/elu-dialog.html',
                        controller: 'EluDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    civilite: null,
                                    nom: null,
                                    prenom: null,
                                    nomJeuneFille: null,
                                    profession: null,
                                    dateNaissance: null,
                                    lieuNaissance: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('elu', null, { reload: true });
                    }, function() {
                        $state.go('elu');
                    })
                }]
            })
            .state('elu.edit', {
                parent: 'elu',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/elu/elu-dialog.html',
                        controller: 'EluDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Elu', function(Elu) {
                                return Elu.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('elu', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('elu.delete', {
                parent: 'elu',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/elu/elu-delete-dialog.html',
                        controller: 'EluDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Elu', function(Elu) {
                                return Elu.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('elu', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
