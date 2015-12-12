'use strict';

describe('AppartenanceGroupePolitique Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockAppartenanceGroupePolitique, MockElu, MockGroupePolitique;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockAppartenanceGroupePolitique = jasmine.createSpy('MockAppartenanceGroupePolitique');
        MockElu = jasmine.createSpy('MockElu');
        MockGroupePolitique = jasmine.createSpy('MockGroupePolitique');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'AppartenanceGroupePolitique': MockAppartenanceGroupePolitique,
            'Elu': MockElu,
            'GroupePolitique': MockGroupePolitique
        };
        createController = function() {
            $injector.get('$controller')("AppartenanceGroupePolitiqueDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'babylone14166App:appartenanceGroupePolitiqueUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
