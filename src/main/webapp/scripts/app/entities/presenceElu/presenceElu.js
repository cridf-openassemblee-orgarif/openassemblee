'use strict';

angular.module('openassembleeApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('presenceElu', {
                parent: 'entity',
                url: '/presenceElus',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'PresenceElus'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/presenceElu/presenceElus.html',
                        controller: 'PresenceEluController'
                    }
                },
                resolve: {
                }
            })
            .state('presenceElu.detail', {
                parent: 'entity',
                url: '/presenceElu/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'PresenceElu'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/presenceElu/presenceElu-detail.html',
                        controller: 'PresenceEluDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'PresenceElu', function($stateParams, PresenceElu) {
                        return PresenceElu.get({id : $stateParams.id});
                    }]
                }
            })
            .state('presenceElu.new', {
                parent: 'presenceElu',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/presenceElu/presenceElu-dialog.html',
                        controller: 'PresenceEluDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('presenceElu', null, { reload: true });
                    }, function() {
                        $state.go('presenceElu');
                    })
                }]
            })
            .state('presenceElu.edit', {
                parent: 'presenceElu',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/presenceElu/presenceElu-dialog.html',
                        controller: 'PresenceEluDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['PresenceElu', function(PresenceElu) {
                                return PresenceElu.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('presenceElu', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('presenceElu.delete', {
                parent: 'presenceElu',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/presenceElu/presenceElu-delete-dialog.html',
                        controller: 'PresenceEluDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['PresenceElu', function(PresenceElu) {
                                return PresenceElu.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('presenceElu', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
