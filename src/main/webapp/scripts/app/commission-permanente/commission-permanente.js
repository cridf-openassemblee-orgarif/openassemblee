angular.module('babylone14166App')
    .config(function ($stateProvider) {
        $stateProvider
            .state('commission-permanente', {
                parent: 'site',
                url: '/commission-permanente',
                data: {
                    authorities: ['ROLE_USER'],
                    // TODO mlo keep ça en fait ?
                    pageTitle: 'Commission permanente'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/commission-permanente/commission-permanente.html',
                        controller: 'CommissionPermanenteController'
                    }
                },
                resolve: {
                    entity: ['CommissionPermanente', function (CommissionPermanente) {
                        return CommissionPermanente.get();
                    }]
                }
            })
    });
