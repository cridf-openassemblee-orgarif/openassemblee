'use strict';

angular.module('openassembleeApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('hemicycle', {
                parent: 'admin',
                url: '/carte-hemicycle',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'Carte de l\'assemblée'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/admin/hemicycle/hemicycle.html',
                        controller: 'HemicycleController'
                    }
                },
                resolve: {

                }
            });
    });
