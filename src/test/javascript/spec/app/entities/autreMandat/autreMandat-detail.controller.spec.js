'use strict';

describe('AutreMandat Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockAutreMandat, MockElu;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockAutreMandat = jasmine.createSpy('MockAutreMandat');
        MockElu = jasmine.createSpy('MockElu');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'AutreMandat': MockAutreMandat,
            'Elu': MockElu
        };
        createController = function() {
            $injector.get('$controller')("AutreMandatDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'babylone14166App:autreMandatUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
