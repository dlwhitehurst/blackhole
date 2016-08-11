(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .controller('GenAccountDialogController', GenAccountDialogController);

    GenAccountDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'GenAccount'];

    function GenAccountDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, GenAccount) {
        var vm = this;

        vm.genAccount = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.genAccount.id !== null) {
                GenAccount.update(vm.genAccount, onSaveSuccess, onSaveError);
            } else {
                GenAccount.save(vm.genAccount, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('blackholeApp:genAccountUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
