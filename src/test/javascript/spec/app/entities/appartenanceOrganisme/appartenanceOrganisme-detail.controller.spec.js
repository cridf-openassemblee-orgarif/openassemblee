'use strict';

describe('AppartenanceOrganisme Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockAppartenanceOrganisme, MockElu;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockAppartenanceOrganisme = jasmine.createSpy('MockAppartenanceOrganisme');
        MockElu = jasmine.createSpy('MockElu');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'AppartenanceOrganisme': MockAppartenanceOrganisme,
            'Elu': MockElu
        };
        createController = function() {
            $injector.get('$controller')("AppartenanceOrganismeDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'babylone14166App:appartenanceOrganismeUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
