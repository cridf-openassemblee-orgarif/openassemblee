'use strict';

describe('HemicyclePlan Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockHemicyclePlan, MockHemicycleConfiguration, MockSeance;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockHemicyclePlan = jasmine.createSpy('MockHemicyclePlan');
        MockHemicycleConfiguration = jasmine.createSpy('MockHemicycleConfiguration');
        MockSeance = jasmine.createSpy('MockSeance');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'HemicyclePlan': MockHemicyclePlan,
            'HemicycleConfiguration': MockHemicycleConfiguration,
            'Seance': MockSeance
        };
        createController = function() {
            $injector.get('$controller')("HemicyclePlanDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'openassembleeApp:hemicyclePlanUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
