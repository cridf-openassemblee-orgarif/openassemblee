'use strict';

angular.module('openassembleeApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


