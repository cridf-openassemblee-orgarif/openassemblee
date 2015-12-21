'use strict';

describe('Organisme Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockOrganisme, MockAdressePostale;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockOrganisme = jasmine.createSpy('MockOrganisme');
        MockAdressePostale = jasmine.createSpy('MockAdressePostale');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Organisme': MockOrganisme,
            'AdressePostale': MockAdressePostale
        };
        createController = function() {
            $injector.get('$controller')("OrganismeDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'babylone14166App:organismeUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
