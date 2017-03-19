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
                        templateUrl: 'scripts/app/entities/groupePolitique/groupePolitique.html',
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
                                    id: null,
                                    adressePostale: {
                                        voie: null,
                                        codePostal: null,
                                        ville: null
                                    }
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('groupesPolitiques.detail', {id: result.id});
                    }, function() {
                        $state.go('groupesPolitiques');
                    })
                }]
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
                        templateUrl: 'scripts/app/entities/groupePolitique/groupePolitique-detail.html',
                        controller: 'GroupePolitiqueDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'GroupePolitique', function ($stateParams, GroupePolitique) {
                        return GroupePolitique.get({id: $stateParams.id});
                    }]
                }
            })
            .state('groupesPolitiques.detail.edit', {
                parent: 'groupesPolitiques.detail',
                url: '/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/groupePolitique/groupePolitique-edit-dialog.html',
                        controller: 'GroupePolitiqueEditDialogController',
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
                        templateUrl: 'scripts/app/entities/groupePolitique/groupePolitique-delete-dialog.html',
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
                        templateUrl: 'scripts/app/entities/groupePolitique/upload-image-dialog.html',
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
