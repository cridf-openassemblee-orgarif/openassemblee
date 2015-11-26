'use strict';

angular.module('babylone14166App')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


