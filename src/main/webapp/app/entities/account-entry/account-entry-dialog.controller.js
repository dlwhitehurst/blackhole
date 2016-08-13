(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .controller('AccountEntryDialogController', AccountEntryDialogController);

    AccountEntryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AccountEntry'];

    function AccountEntryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, AccountEntry) {
        var vm = this;

        vm.accountEntry = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.accountEntry.id !== null) {
                AccountEntry.update(vm.accountEntry, onSaveSuccess, onSaveError);
            } else {
                AccountEntry.save(vm.accountEntry, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('blackholeApp:accountEntryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.entrydate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
