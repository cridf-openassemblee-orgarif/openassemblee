'use strict';

describe('PresenceElu Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockPresenceElu, MockElu, MockSignature, MockSeance;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockPresenceElu = jasmine.createSpy('MockPresenceElu');
        MockElu = jasmine.createSpy('MockElu');
        MockSignature = jasmine.createSpy('MockSignature');
        MockSeance = jasmine.createSpy('MockSeance');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'PresenceElu': MockPresenceElu,
            'Elu': MockElu,
            'Signature': MockSignature,
            'Seance': MockSeance
        };
        createController = function() {
            $injector.get('$controller')("PresenceEluDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'babylone14166App:presenceEluUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
