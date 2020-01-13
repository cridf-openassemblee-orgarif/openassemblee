'use strict';

angular.module('openassembleeApp')
.controller('MainController', function ($scope, Principal, $http) {
    Principal.identity().then(function (account) {
        $scope.account = account;
        $scope.isAuthenticated = Principal.isAuthenticated;

        $http({
            method: 'GET',
            url: 'search/test'
        }).then(function successCallback(result) {
            $scope.assemblee = result.data;
            console.log($scope.assemblee);
        }, function errorCallback(response) {
        });

        $scope.test = function() {
            console.log('test')
        }

    });
});
