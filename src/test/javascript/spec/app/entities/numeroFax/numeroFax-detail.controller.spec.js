'use strict';

describe('NumeroFax Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockNumeroFax;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockNumeroFax = jasmine.createSpy('MockNumeroFax');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'NumeroFax': MockNumeroFax
        };
        createController = function() {
            $injector.get('$controller')("NumeroFaxDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'babylone14166App:numeroFaxUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
