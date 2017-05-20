'use strict';

describe('Seance Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockSeance, MockPresenceElu;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockSeance = jasmine.createSpy('MockSeance');
        MockPresenceElu = jasmine.createSpy('MockPresenceElu');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Seance': MockSeance,
            'PresenceElu': MockPresenceElu
        };
        createController = function() {
            $injector.get('$controller')("SeanceDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'openassembleeApp:seanceUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
