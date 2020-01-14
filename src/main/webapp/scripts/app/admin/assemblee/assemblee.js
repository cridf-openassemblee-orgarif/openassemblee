'use strict';

angular.module('openassembleeApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('assemblee', {
                parent: 'admin',
                url: '/carte-assemblee',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'Carte de l\'assemblée'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/admin/assemblee/assemblee.html',
                        controller: 'AssembleeController'
                    }
                },
                resolve: {

                }
            });
    });
