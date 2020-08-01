'use strict';

angular.module('openassembleeApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('hemicycleArchive', {
                parent: 'entity',
                url: '/hemicycleArchives',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'HemicycleArchives'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/hemicycleArchive/hemicycleArchives.html',
                        controller: 'HemicycleArchiveController'
                    }
                },
                resolve: {
                }
            })
            .state('hemicycleArchive.detail', {
                parent: 'entity',
                url: '/hemicycleArchive/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'HemicycleArchive'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/hemicycleArchive/hemicycleArchive-detail.html',
                        controller: 'HemicycleArchiveDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'HemicycleArchive', function($stateParams, HemicycleArchive) {
                        return HemicycleArchive.get({id : $stateParams.id});
                    }]
                }
            })
            .state('hemicycleArchive.new', {
                parent: 'hemicycleArchive',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/hemicycleArchive/hemicycleArchive-dialog.html',
                        controller: 'HemicycleArchiveDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    jsonPlan: null,
                                    svgPlan: null,
                                    date: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('hemicycleArchive', null, { reload: true });
                    }, function() {
                        $state.go('hemicycleArchive');
                    })
                }]
            })
            .state('hemicycleArchive.edit', {
                parent: 'hemicycleArchive',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/hemicycleArchive/hemicycleArchive-dialog.html',
                        controller: 'HemicycleArchiveDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['HemicycleArchive', function(HemicycleArchive) {
                                return HemicycleArchive.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('hemicycleArchive', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('hemicycleArchive.delete', {
                parent: 'hemicycleArchive',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/hemicycleArchive/hemicycleArchive-delete-dialog.html',
                        controller: 'HemicycleArchiveDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['HemicycleArchive', function(HemicycleArchive) {
                                return HemicycleArchive.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('hemicycleArchive', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
