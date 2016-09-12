'use strict';

angular.module('babylone14166App')
    .controller('AuditTrailController', function ($scope, $state, $modal, AuditTrail, AuditTrailSearch, ParseLinks) {
      
        $scope.auditTrails = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            AuditTrail.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.auditTrails = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            AuditTrailSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.auditTrails = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.auditTrail = {
                entity: null,
                entityId: null,
                parentEntity: null,
                parentEntityId: null,
                action: null,
                user: null,
                date: null,
                details: null,
                reason: null,
                id: null
            };
        };
    });
