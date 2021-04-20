'use strict';

describe('Mandat Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockMandat, MockElu, MockMandature, MockListeElectorale;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockMandat = jasmine.createSpy('MockMandat');
        MockElu = jasmine.createSpy('MockElu');
        MockMandature = jasmine.createSpy('MockMandature');
        MockListeElectorale = jasmine.createSpy('MockListeElectorale');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Mandat': MockMandat,
            'Elu': MockElu,
            'Mandature': MockMandature,
            'ListeElectorale': MockListeElectorale
        };
        createController = function() {
            $injector.get('$controller')("MandatDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'openassembleeApp:mandatUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
