'use strict';

describe('AppartenanceCommissionThematique Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockAppartenanceCommissionThematique, MockElu, MockCommissionThematique;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockAppartenanceCommissionThematique = jasmine.createSpy('MockAppartenanceCommissionThematique');
        MockElu = jasmine.createSpy('MockElu');
        MockCommissionThematique = jasmine.createSpy('MockCommissionThematique');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'AppartenanceCommissionThematique': MockAppartenanceCommissionThematique,
            'Elu': MockElu,
            'CommissionThematique': MockCommissionThematique
        };
        createController = function() {
            $injector.get('$controller')("AppartenanceCommissionThematiqueDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'openassembleeApp:appartenanceCommissionThematiqueUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
