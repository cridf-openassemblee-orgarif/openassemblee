'use strict';

angular.module('babylone14166App')
    .config(function ($stateProvider) {
        $stateProvider
            .state('groupePolitique', {
                parent: 'entity',
                url: '/groupePolitiques',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'GroupePolitiques'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/groupePolitique/groupePolitiques.html',
                        controller: 'GroupePolitiqueController'
                    }
                },
                resolve: {
                }
            })
            .state('groupePolitique.detail', {
                parent: 'entity',
                url: '/groupePolitique/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'GroupePolitique'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/groupePolitique/groupePolitique-detail.html',
                        controller: 'GroupePolitiqueDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'GroupePolitique', function($stateParams, GroupePolitique) {
                        return GroupePolitique.get({id : $stateParams.id});
                    }]
                }
            })
            .state('groupePolitique.new', {
                parent: 'groupePolitique',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/groupePolitique/groupePolitique-dialog.html',
                        controller: 'GroupePolitiqueDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    nom: null,
                                    nomCourt: null,
                                    dateDebut: null,
                                    dateFin: null,
                                    motifFin: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('groupePolitique', null, { reload: true });
                    }, function() {
                        $state.go('groupePolitique');
                    })
                }]
            })
            .state('groupePolitique.edit', {
                parent: 'groupePolitique',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/groupePolitique/groupePolitique-dialog.html',
                        controller: 'GroupePolitiqueDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['GroupePolitique', function(GroupePolitique) {
                                return GroupePolitique.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('groupePolitique', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('groupePolitique.delete', {
                parent: 'groupePolitique',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/groupePolitique/groupePolitique-delete-dialog.html',
                        controller: 'GroupePolitiqueDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['GroupePolitique', function(GroupePolitique) {
                                return GroupePolitique.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('groupePolitique', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
