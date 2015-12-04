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
                    pageTitle: "Ajout d'un élu"
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/elus/elu-edit.html',
                        controller: 'EluEditController'
                    }
                },
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
            })
            .state('groupesPolitiques.elu', {
                parent: 'groupesPolitiques',
                url: '/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Élu'
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
            .state('groupesPolitiques.delete', {
                parent: 'groupesPolitiques',
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
