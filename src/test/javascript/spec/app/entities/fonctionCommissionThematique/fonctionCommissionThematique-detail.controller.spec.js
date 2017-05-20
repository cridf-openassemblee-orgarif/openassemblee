'use strict';

describe('FonctionCommissionThematique Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockFonctionCommissionThematique, MockElu, MockCommissionThematique;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockFonctionCommissionThematique = jasmine.createSpy('MockFonctionCommissionThematique');
        MockElu = jasmine.createSpy('MockElu');
        MockCommissionThematique = jasmine.createSpy('MockCommissionThematique');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'FonctionCommissionThematique': MockFonctionCommissionThematique,
            'Elu': MockElu,
            'CommissionThematique': MockCommissionThematique
        };
        createController = function() {
            $injector.get('$controller')("FonctionCommissionThematiqueDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'openassembleeApp:fonctionCommissionThematiqueUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
