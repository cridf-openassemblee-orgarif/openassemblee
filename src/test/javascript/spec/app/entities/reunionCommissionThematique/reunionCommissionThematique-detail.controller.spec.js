'use strict';

describe('ReunionCommissionThematique Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockReunionCommissionThematique;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockReunionCommissionThematique = jasmine.createSpy('MockReunionCommissionThematique');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'ReunionCommissionThematique': MockReunionCommissionThematique
        };
        createController = function() {
            $injector.get('$controller')("ReunionCommissionThematiqueDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'openassembleeApp:reunionCommissionThematiqueUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
