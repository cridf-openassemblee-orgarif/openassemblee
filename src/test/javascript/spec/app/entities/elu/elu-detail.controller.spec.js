'use strict';

describe('Elu Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockElu, MockAdressePostale;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockElu = jasmine.createSpy('MockElu');
        MockAdressePostale = jasmine.createSpy('MockAdressePostale');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Elu': MockElu,
            'AdressePostale': MockAdressePostale
        };
        createController = function() {
            $injector.get('$controller')("EluDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'babylone14166App:eluUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
