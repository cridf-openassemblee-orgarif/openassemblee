'use strict';

describe('HemicycleConfiguration Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockHemicycleConfiguration;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockHemicycleConfiguration = jasmine.createSpy('MockHemicycleConfiguration');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'HemicycleConfiguration': MockHemicycleConfiguration
        };
        createController = function() {
            $injector.get('$controller')("HemicycleConfigurationDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'openassembleeApp:hemicycleConfigurationUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
