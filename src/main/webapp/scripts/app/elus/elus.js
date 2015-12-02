'use strict';

angular.module('babylone14166App')
    .config(function ($stateProvider) {
        $stateProvider
            .state('elus', {
                parent: 'site',
                url: '/elus',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Elus'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/elus/elus.html',
                        controller: 'ElusController'
                    }
                },
                resolve: {}
            })
            .state('elu.detail', {
                parent: 'site',
                url: '/elu/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Elu'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/elus/elu-detail.html',
                        controller: 'EluDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Elu', function ($stateParams, Elu) {
                        return Elu.get({id: $stateParams.id});
                    }]
                }
            })
            .state('elu.delete', {
                parent: 'elus',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/elus/elu-delete-dialog.html',
                        controller: 'EluDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Elu', function (Elu) {
                                return Elu.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function (result) {
                            $state.go('elu', null, {reload: true});
                        }, function () {
                            $state.go('^');
                        })
                }]
            });
    });
