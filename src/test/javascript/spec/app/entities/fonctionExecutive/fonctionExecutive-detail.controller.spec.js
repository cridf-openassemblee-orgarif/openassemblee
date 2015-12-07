'use strict';

describe('FonctionExecutive Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockFonctionExecutive, MockElu;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockFonctionExecutive = jasmine.createSpy('MockFonctionExecutive');
        MockElu = jasmine.createSpy('MockElu');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'FonctionExecutive': MockFonctionExecutive,
            'Elu': MockElu
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
