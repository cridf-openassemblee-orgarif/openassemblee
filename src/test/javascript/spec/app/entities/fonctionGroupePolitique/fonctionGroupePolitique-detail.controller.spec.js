'use strict';

describe('FonctionGroupePolitique Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockFonctionGroupePolitique, MockElu, MockGroupePolitique;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockFonctionGroupePolitique = jasmine.createSpy('MockFonctionGroupePolitique');
        MockElu = jasmine.createSpy('MockElu');
        MockGroupePolitique = jasmine.createSpy('MockGroupePolitique');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'FonctionGroupePolitique': MockFonctionGroupePolitique,
            'Elu': MockElu,
            'GroupePolitique': MockGroupePolitique
        };
        createController = function() {
            $injector.get('$controller')("FonctionGroupePolitiqueDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'babylone14166App:fonctionGroupePolitiqueUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
