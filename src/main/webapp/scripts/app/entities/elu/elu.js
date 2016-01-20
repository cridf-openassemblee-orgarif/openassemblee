'use strict';

angular.module('babylone14166App')
    .config(function ($stateProvider) {
        $stateProvider
            .state('elu', {
                parent: 'entity',
                url: '/elus',
                data: {
                    authorities: ['ROLE_USER'],
                    // TODO mlo keep ça en fait ?
                    pageTitle: 'Élus'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/elu/elus.html',
                        controller: 'EluController'
                    }
                },
                resolve: {}
            })
            .state('elu.new', {
                parent: 'elu',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/elu/elu-dialog.html',
                        controller: 'EluDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    elu: {
                                        civilite: null,
                                        nom: null,
                                        prenom: null,
                                        nomJeuneFille: null,
                                        profession: null,
                                        dateNaissance: null,
                                        lieuNaissance: null,
                                        id: null
                                    }
                                };
                            }
                        }
                    }).result.then(function (result) {
                        console.log(result.id);
                        $state.go('elu.detail', {id: result.id}, {reload: true});
                    }, function () {
                        $state.go('elu');
                    })
                }]
            })
            .state('elu.detail', {
                parent: 'elu',
                url: '/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Élu'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/elu/elu-detail.html',
                        controller: 'EluDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Elu', function ($stateParams, Elu) {
                        return Elu.get({id: $stateParams.id});
                    }]
                }
            })
            .state('elu.edit', {
                parent: 'elu.detail',
                url: '/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/elu/elu-dialog.html',
                        controller: 'EluDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Elu', function (Elu) {
                                return Elu.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function (result) {
                        $state.go('^', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    })
                }]
            })
            .state('elu.delete', {
                parent: 'elu',
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
            .state('elu.detail.nouvelleFonctionExecutive', {
                parent: 'elu.detail',
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
            .state('elu.detail.finFonctionExecutive', {
                parent: 'elu.detail',
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
            .state('elu.detail.nouvelleFonctionCommissionPermanente', {
                parent: 'elu.detail',
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
            .state('elu.detail.finFonctionCommissionPermanente', {
                parent: 'elu.detail',
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
            .state('elu.detail.ajouterCommissionPermanente', {
                parent: 'elu.detail',
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
            .state('elu.detail.finAppartenanceCommissionPermanente', {
                parent: 'elu.detail',
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
            .state('elu.detail.ajouterGroupePolitique', {
                parent: 'elu.detail',
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
            .state('elu.detail.finAppartenanceGroupePolitique', {
                parent: 'elu.detail',
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
            .state('elu.detail.ajouterOrganisme', {
                parent: 'elu.detail',
                url: '/ajouter-organisme',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/appartenanceOrganisme/appartenanceOrganisme-dialog.html',
                        controller: 'AppartenanceOrganismeDialogController',
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
            .state('elu.detail.finAppartenanceOrganisme', {
                parent: 'elu.detail',
                url: '/fin-appartenance-organisme/{appartenanceId}',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/appartenanceOrganisme/appartenanceOrganisme-fin-dialog.html',
                        controller: 'AppartenanceOrganismeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['AppartenanceOrganisme', function (AppartenanceOrganisme) {
                                return AppartenanceOrganisme.get({id: $stateParams.appartenanceId});
                            }]
                        }
                    }).result.then(function (result) {
                        $state.go('^', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    })
                }]
            })
            .state('elu.detail.nouvelleFonctionGroupePolitique', {
                parent: 'elu.detail',
                url: '/nouvelle-fonction-groupe-politique/{groupePolitiqueId}',
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
                                    groupePolitique: {id: $stateParams.groupePolitiqueId},
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
            .state('elu.detail.finFonctionGroupePolitique', {
                parent: 'elu.detail',
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
            .state('elu.detail.ajouterCommissionThematique', {
                parent: 'elu.detail',
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
            .state('elu.detail.finAppartenanceCommissionThematique', {
                parent: 'elu.detail',
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
            .state('elu.detail.nouvelleFonctionCommissionThematique', {
                parent: 'elu.detail',
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
            .state('elu.detail.finFonctionCommissionThematique', {
                parent: 'elu.detail',
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
            })
            .state('elu.detail.uploadImage', {
                parent: 'elu.detail',
                url: '/upload-image',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/elus/upload-image-dialog.html',
                        controller: 'UploadImageEluDialogController',
                        size: 'lg'
                    }).result.then(function (result) {
                        $state.go('^', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    })
                }]
            });
    })
