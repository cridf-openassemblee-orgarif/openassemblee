'use strict';

describe('ListeElectorale Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockListeElectorale, MockMandature;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockListeElectorale = jasmine.createSpy('MockListeElectorale');
        MockMandature = jasmine.createSpy('MockMandature');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'ListeElectorale': MockListeElectorale,
            'Mandature': MockMandature
        };
        createController = function() {
            $injector.get('$controller')("ListeElectoraleDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'openassembleeApp:listeElectoraleUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
