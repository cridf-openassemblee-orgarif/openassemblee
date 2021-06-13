angular.module("openassembleeApp").config(function ($stateProvider) {
    $stateProvider.state("ajouterElusMandature", {
        parent: "site",
        url: "/ajouter-elus-mandature/{id}",
        data: {
            // TODO mlo keep ça en fait ?
            pageTitle: "Nouveaux élus",
        },
        views: {
            "content@": {
                templateUrl:
                    "scripts/app/ajouter-elus-mandature/ajouter-elus-mandature.html",
                controller: "AjouterElusMandature",
            },
        },
        resolve: {
            // entity: ['NouvelleMandature', function (NouvelleMandature) {
            //     return CommissionPermanente.get();
            // }]
        },
    });
});
