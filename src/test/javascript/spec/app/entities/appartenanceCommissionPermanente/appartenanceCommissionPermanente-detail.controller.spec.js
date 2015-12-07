'use strict';

describe('AppartenanceCommissionPermanente Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockAppartenanceCommissionPermanente;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockAppartenanceCommissionPermanente = jasmine.createSpy('MockAppartenanceCommissionPermanente');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'AppartenanceCommissionPermanente': MockAppartenanceCommissionPermanente
        };
        createController = function() {
            $injector.get('$controller')("AppartenanceCommissionPermanenteDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'babylone14166App:appartenanceCommissionPermanenteUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
