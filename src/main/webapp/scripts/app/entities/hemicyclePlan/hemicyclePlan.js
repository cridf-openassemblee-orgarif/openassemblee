'use strict';

angular.module('openassembleeApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('hemicyclePlan', {
                parent: 'entity',
                url: '/hemicyclePlans',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'HemicyclePlans'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/hemicyclePlan/hemicyclePlans.html',
                        controller: 'HemicyclePlanController'
                    }
                },
                resolve: {
                }
            })
            .state('hemicyclePlan.detail', {
                parent: 'entity',
                url: '/hemicyclePlan/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'HemicyclePlan'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/hemicyclePlan/hemicyclePlan-detail.html',
                        controller: 'HemicyclePlanDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'HemicyclePlan', function($stateParams, HemicyclePlan) {
                        return HemicyclePlan.get({id : $stateParams.id});
                    }]
                }
            })
            .state('hemicyclePlan.new', {
                parent: 'hemicyclePlan',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/hemicyclePlan/hemicyclePlan-dialog.html',
                        controller: 'HemicyclePlanDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    label: null,
                                    jsonPlan: null,
                                    creationDate: null,
                                    lastModificationDate: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('hemicyclePlan', null, { reload: true });
                    }, function() {
                        $state.go('hemicyclePlan');
                    })
                }]
            })
            .state('hemicyclePlan.edit', {
                parent: 'hemicyclePlan',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/hemicyclePlan/hemicyclePlan-dialog.html',
                        controller: 'HemicyclePlanDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['HemicyclePlan', function(HemicyclePlan) {
                                return HemicyclePlan.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('hemicyclePlan', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('hemicyclePlan.delete', {
                parent: 'hemicyclePlan',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/hemicyclePlan/hemicyclePlan-delete-dialog.html',
                        controller: 'HemicyclePlanDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['HemicyclePlan', function(HemicyclePlan) {
                                return HemicyclePlan.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('hemicyclePlan', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
