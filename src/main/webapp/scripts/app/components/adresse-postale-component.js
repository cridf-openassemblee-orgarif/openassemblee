myModule.directive('zippy', function () {
    return {
        restrict: 'E',
        template: '<div><div class="header"></div><div class="content" ng-transclude></div></div>',
        link: function (scope, el) {
            el.find('.header').click(function () {
                el.find('.content').toggle();
            });
        }
    }
});
