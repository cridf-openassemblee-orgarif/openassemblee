'use strict';

describe('GroupePolitique Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockGroupePolitique;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockGroupePolitique = jasmine.createSpy('MockGroupePolitique');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'GroupePolitique': MockGroupePolitique
        };
        createController = function() {
            $injector.get('$controller')("GroupePolitiqueDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'babylone14166App:groupePolitiqueUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
