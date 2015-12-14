'use strict';

angular.module('babylone14166App').controller('UploadImageDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Upload',
        function ($scope, $stateParams, $modalInstance, entity, Upload) {

            $scope.submit = function () {
                //if (form.file.$valid && $scope.file) {
                $scope.upload($scope.file);
                //}
            };

            // upload on file select or drop
            $scope.upload = function (file) {
                console.log(file)
                Upload.upload({
                    url: 'api/elus/' + $stateParams.id + '/image',
                    data: {'name': 'test'},
                    file: file
                }).then(function (resp) {
                    $state.go('^', null, {reload: true});
                }, function (resp) {
                    // TODO notif
                    console.log('Error status: ' + resp.status);
                }, function (evt) {
                    //var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                });
            };

            $scope.clear = function () {
                $modalInstance.dismiss('cancel');
            };
        }]);
