'use strict';

angular.module('babylone14166App')
    .config(function ($stateProvider) {
        $stateProvider
            .state('groupesPolitiques', {
                parent: 'site',
                url: '/groupes-politiques',
                data: {
                    authorities: ['ROLE_USER'],
                    // TODO mlo keep ça en fait ?
                    pageTitle: 'Groupes politiques'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/groupes-politiques/groupes-politiques.html',
                        controller: 'GroupesPolitiquesController'
                    }
                },
                resolve: {}
            })
            .state('groupesPolitiques.new', {
                parent: 'groupesPolitiques',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: "Ajout d'un groupe politique"
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/groupes-politiques/groupes-politiques-edit.html',
                        controller: 'GroupePolitiqueEditController'
                    }
                },
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
            })
            .state('groupesPolitiques.detail', {
                parent: 'groupesPolitiques',
                url: '/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Groupe politique'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/groupes-politiques/groupes-politiques-detail.html',
                        controller: 'GroupePolitiqueDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'GroupePolitique', function ($stateParams, GroupePolitique) {
                        return GroupePolitique.get({id: $stateParams.id});
                    }]
                }
            })
            .state('groupesPolitiques.detail.fin', {
                parent: 'groupesPolitiques.detail',
                url: '/fin',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/groupes-politiques/groupes-politiques-fin-dialog.html',
                        controller: 'GroupePolitiqueDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['GroupePolitique', function (GroupePolitique) {
                                return GroupePolitique.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function (result) {
                        $state.go('^', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    })
                }]
            })
            .state('groupesPolitiques.delete', {
                parent: 'groupesPolitiques',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/groupes-politiques/groupes-politiques-delete-dialog.html',
                        controller: 'GroupePolitiqueDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['GroupePolitique', function (GroupePolitique) {
                                return GroupePolitique.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function (result) {
                        $state.go('groupePolitique', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    })
                }]
            })
            .state('groupesPolitiques.detail.uploadImage', {
                parent: 'groupesPolitiques.detail',
                url: '/upload-image',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/groupes-politiques/upload-image-dialog.html',
                        controller: 'UploadImageGroupePolitiqueDialogController',
                        size: 'lg'
                    }).result.then(function (result) {
                        $state.go('^', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    })
                }]
            });
    });
