'use strict';

angular.module('openassembleeApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('commissionThematique', {
                parent: 'entity',
                url: '/commissions-thematiques',
                data: {
                    pageTitle: 'CommissionThematiques'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/commissionThematique/commissionThematiques.html',
                        controller: 'CommissionThematiqueController'
                    }
                },
                resolve: {}
            })
            .state('commissionThematique.new', {
                parent: 'commissionThematique',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/commissionThematique/commissionThematique-dialog.html',
                        controller: 'CommissionThematiqueDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    nom: null,
                                    dateDebut: null,
                                    dateFin: null,
                                    motifFin: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function (result) {
                        $state.go('commissionThematique.detail', {id: result.id}, {reload: true});
                    }, function () {
                        $state.go('commissionThematique');
                    })
                }]
            })
            .state('commissionThematique.detail', {
                parent: 'commissionThematique',
                url: '/{id}',
                data: {
                    pageTitle: 'CommissionThematique'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/commissionThematique/commissionThematique-detail.html',
                        controller: 'CommissionThematiqueDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'CommissionThematique', function ($stateParams, CommissionThematique) {
                        return CommissionThematique.get({id: $stateParams.id});
                    }]
                }
            })
            .state('commissionThematique.edit', {
                parent: 'commissionThematique',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/commissionThematique/commissionThematique-dialog.html',
                        controller: 'CommissionThematiqueDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['CommissionThematique', function (CommissionThematique) {
                                return CommissionThematique.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function (result) {
                        $state.go('commissionThematique', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    })
                }]
            })
            .state('commissionThematique.delete', {
                parent: 'commissionThematique',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/commissionThematique/commissionThematique-delete-dialog.html',
                        controller: 'CommissionThematiqueDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['CommissionThematique', function (CommissionThematique) {
                                return CommissionThematique.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function (result) {
                        $state.go('commissionThematique', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    })
                }]
            })
            .state('commissionThematique.detail.fin', {
                parent: 'commissionThematique.detail',
                url: '/fin',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/commissionThematique/commissionThematique-fin-dialog.html',
                        controller: 'CommissionThematiqueDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['CommissionThematique', function (CommissionThematique) {
                                return CommissionThematique.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function (result) {
                        $state.go('^', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    })
                }]
            });
    });
