'use strict';

describe('HemicycleArchive Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockHemicycleArchive;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockHemicycleArchive = jasmine.createSpy('MockHemicycleArchive');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'HemicycleArchive': MockHemicycleArchive
        };
        createController = function() {
            $injector.get('$controller')("HemicycleArchiveDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'openassembleeApp:hemicycleArchiveUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
