angular.module("openassembleeApp").config(function ($stateProvider) {
    $stateProvider.state("executif", {
        parent: "site",
        url: "/executif",
        data: {
            pageTitle: "Exécutif",
        },
        views: {
            "content@": {
                templateUrl: "scripts/app/executif/executif.html",
                controller: "ExecutifController",
            },
        },
        resolve: {
            entity: [
                "CommissionPermanente",
                function (CommissionPermanente) {
                    return CommissionPermanente.getExecutif();
                },
            ],
        },
    });
});
