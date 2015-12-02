'use strict';

describe('AdressePostale Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockAdressePostale;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockAdressePostale = jasmine.createSpy('MockAdressePostale');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'AdressePostale': MockAdressePostale
        };
        createController = function() {
            $injector.get('$controller')("AdressePostaleDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'babylone14166App:adressePostaleUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
