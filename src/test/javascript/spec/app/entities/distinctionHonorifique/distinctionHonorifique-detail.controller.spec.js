'use strict';

describe('DistinctionHonorifique Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockDistinctionHonorifique, MockElu;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockDistinctionHonorifique = jasmine.createSpy('MockDistinctionHonorifique');
        MockElu = jasmine.createSpy('MockElu');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'DistinctionHonorifique': MockDistinctionHonorifique,
            'Elu': MockElu
        };
        createController = function() {
            $injector.get('$controller')("DistinctionHonorifiqueDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'openassembleeApp:distinctionHonorifiqueUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
