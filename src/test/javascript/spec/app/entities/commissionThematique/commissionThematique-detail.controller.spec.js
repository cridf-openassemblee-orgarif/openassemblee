'use strict';

describe('CommissionThematique Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockCommissionThematique;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockCommissionThematique = jasmine.createSpy('MockCommissionThematique');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'CommissionThematique': MockCommissionThematique
        };
        createController = function() {
            $injector.get('$controller')("CommissionThematiqueDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'babylone14166App:commissionThematiqueUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
