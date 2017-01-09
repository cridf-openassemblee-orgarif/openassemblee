'use strict';

describe('Signature Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockSignature, MockPresenceElu;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockSignature = jasmine.createSpy('MockSignature');
        MockPresenceElu = jasmine.createSpy('MockPresenceElu');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Signature': MockSignature,
            'PresenceElu': MockPresenceElu
        };
        createController = function() {
            $injector.get('$controller')("SignatureDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'babylone14166App:signatureUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
