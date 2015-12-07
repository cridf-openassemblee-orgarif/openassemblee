'use strict';

describe('FonctionExecutive Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockFonctionExecutive;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockFonctionExecutive = jasmine.createSpy('MockFonctionExecutive');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'FonctionExecutive': MockFonctionExecutive
        };
        createController = function() {
            $injector.get('$controller')("FonctionExecutiveDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'babylone14166App:fonctionExecutiveUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
