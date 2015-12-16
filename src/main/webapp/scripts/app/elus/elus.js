'use strict';

angular.module('babylone14166App')
    .config(function ($stateProvider) {
        $stateProvider
            .state('elus', {
                parent: 'site',
                url: '/elus',
                data: {
                    authorities: ['ROLE_USER'],
                    // TODO mlo keep ça en fait ?
                    pageTitle: 'Élus'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/elus/elus.html',
                        controller: 'ElusController'
                    }
                },
                resolve: {}
            })
            .state('elus.new', {
                parent: 'elus',
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
            .state('elus.detail', {
                parent: 'elus',
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
                    entity: ['$stateParams', 'EluComplet', function ($stateParams, EluComplet) {
                        return EluComplet.get({id: $stateParams.id});
                    }]
                }
            })
            .state('elus.delete', {
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
            })
            .state('elus.detail.nouvelleFonctionExecutive', {
                parent: 'elus.detail',
                url: '/nouvelle-fonction-executive',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/elus/fonctionExecutive-dialog.html',
                        controller: 'FonctionExecutiveDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    fonction: null,
                                    dateDebut: null,
                                    dateFin: null,
                                    motifFin: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function (result) {
                        $state.go('^', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    })
                }]
            })
            .state('elus.detail.finFonctionExecutive', {
                parent: 'elus.detail',
                url: '/fin-fonction-executive/{fonctionId}',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/elus/fonctionExecutive-fin-dialog.html',
                        controller: 'FonctionExecutiveDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['FonctionExecutive', function (FonctionExecutive) {
                                return FonctionExecutive.get({id: $stateParams.fonctionId});
                            }]
                        }
                    }).result.then(function (result) {
                        $state.go('^', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    })
                }]
            })
            .state('elus.detail.nouvelleFonctionCommissionPermanente', {
                parent: 'elus.detail',
                url: '/nouvelle-fonction-commission-permanente',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/elus/fonctionCommissionPermanente-dialog.html',
                        controller: 'FonctionCommissionPermanenteDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    fonction: null,
                                    dateDebut: null,
                                    dateFin: null,
                                    motifFin: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function (result) {
                        $state.go('^', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    })
                }]
            })
            .state('elus.detail.finFonctionCommissionPermanente', {
                parent: 'elus.detail',
                url: '/fin-fonction-commission-permanente/{fonctionId}',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/elus/fonctionCommissionPermanente-fin-dialog.html',
                        controller: 'FonctionCommissionPermanenteDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['FonctionCommissionPermanente', function (FonctionCommissionPermanente) {
                                return FonctionCommissionPermanente.get({id: $stateParams.fonctionId});
                            }]
                        }
                    }).result.then(function (result) {
                        $state.go('^', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    })
                }]
            })
            .state('elus.detail.ajouterCommissionPermanente', {
                parent: 'elus.detail',
                url: '/ajouter-commission-permanente',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/elus/appartenanceCommissionPermanente-dialog.html',
                        controller: 'AppartenanceCommissionPermanenteDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    dateDebut: null,
                                    dateFin: null,
                                    motifFin: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function (result) {
                        $state.go('^', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    })
                }]
            })
            .state('elus.detail.finAppartenanceCommissionPermanente', {
                parent: 'elus.detail',
                url: '/fin-appartenance-commission-permanente/{appartenanceId}',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/elus/appartenanceCommissionPermanente-fin-dialog.html',
                        controller: 'AppartenanceCommissionPermanenteDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['AppartenanceCommissionPermanente', function (AppartenanceCommissionPermanente) {
                                return AppartenanceCommissionPermanente.get({id: $stateParams.appartenanceId});
                            }]
                        }
                    }).result.then(function (result) {
                        $state.go('^', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    })
                }]
            })
            .state('elus.detail.ajouterGroupePolitique', {
                parent: 'elus.detail',
                url: '/ajouter-groupe-politique',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/elus/appartenanceGroupePolitique-dialog.html',
                        controller: 'AppartenanceGroupePolitiqueDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    dateDebut: null,
                                    dateFin: null,
                                    motifFin: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function (result) {
                        $state.go('^', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    })
                }]
            })
            .state('elus.detail.finAppartenanceGroupePolitique', {
                parent: 'elus.detail',
                url: '/fin-appartenance-groupe-politique/{appartenanceId}',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/elus/appartenanceGroupePolitique-fin-dialog.html',
                        controller: 'AppartenanceGroupePolitiqueDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['AppartenanceGroupePolitique', function (AppartenanceGroupePolitique) {
                                return AppartenanceGroupePolitique.get({id: $stateParams.appartenanceId});
                            }]
                        }
                    }).result.then(function (result) {
                        $state.go('^', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    })
                }]
            })
            .state('elus.detail.nouvelleFonctionGroupePolitique', {
                parent: 'elus.detail',
                url: '/nouvelle-fonction-groupe-politique',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/elus/fonctionGroupePolitique-dialog.html',
                        controller: 'FonctionGroupePolitiqueDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    fonction: null,
                                    dateDebut: null,
                                    dateFin: null,
                                    motifFin: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function (result) {
                        $state.go('^', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    })
                }]
            })
            .state('elus.detail.finFonctionGroupePolitique', {
                parent: 'elus.detail',
                url: '/fin-fonction-groupe-politique/{fonctionId}',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/elus/fonctionGroupePolitique-fin-dialog.html',
                        controller: 'FonctionGroupePolitiqueDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['FonctionGroupePolitique', function (FonctionGroupePolitique) {
                                return FonctionGroupePolitique.get({id: $stateParams.fonctionId});
                            }]
                        }
                    }).result.then(function (result) {
                        $state.go('^', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    })
                }]
            })
            .state('elus.detail.ajouterCommissionThematique', {
                parent: 'elus.detail',
                url: '/ajouter-commission-thematique',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/appartenanceCommissionThematique/appartenanceCommissionThematique-dialog.html',
                        controller: 'AppartenanceCommissionThematiqueDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {};
                            }
                        }
                    }).result.then(function (result) {
                        $state.go('^', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    })
                }]
            })
            .state('elus.detail.finAppartenanceCommissionThematique', {
                parent: 'elus.detail',
                url: '/fin-appartenance-commission-thematique/{appartenanceId}',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/appartenanceCommissionThematique/appartenanceCommissionThematique-fin-dialog.html',
                        controller: 'AppartenanceCommissionThematiqueDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['AppartenanceCommissionThematique', function (AppartenanceCommissionThematique) {
                                return AppartenanceCommissionThematique.get({id: $stateParams.appartenanceId});
                            }]
                        }
                    }).result.then(function (result) {
                        $state.go('^', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    })
                }]
            })
            .state('elus.detail.nouvelleFonctionCommissionThematique', {
                parent: 'elus.detail',
                url: '/nouvelle-fonction-commission-thematique',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/fonctionCommissionThematique/fonctionCommissionThematique-dialog.html',
                        controller: 'FonctionCommissionThematiqueDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {};
                            }
                        }
                    }).result.then(function (result) {
                        $state.go('^', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    })
                }]
            })
            .state('elus.detail.finFonctionCommissionThematique', {
                parent: 'elus.detail',
                url: '/fin-fonction-commission-thematique/{fonctionId}',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/fonctionCommissionThematique/fonctionCommissionThematique-fin-dialog.html',
                        controller: 'FonctionCommissionThematiqueDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['FonctionCommissionThematique', function (FonctionCommissionThematique) {
                                return FonctionCommissionThematique.get({id: $stateParams.fonctionId});
                            }]
                        }
                    }).result.then(function (result) {
                        $state.go('^', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    })
                }]
            });
    })
