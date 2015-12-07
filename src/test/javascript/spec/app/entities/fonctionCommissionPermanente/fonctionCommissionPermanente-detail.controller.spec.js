'use strict';

describe('FonctionCommissionPermanente Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockFonctionCommissionPermanente, MockElu;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockFonctionCommissionPermanente = jasmine.createSpy('MockFonctionCommissionPermanente');
        MockElu = jasmine.createSpy('MockElu');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'FonctionCommissionPermanente': MockFonctionCommissionPermanente,
            'Elu': MockElu
        };
        createController = function() {
            $injector.get('$controller')("FonctionCommissionPermanenteDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'babylone14166App:fonctionCommissionPermanenteUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});