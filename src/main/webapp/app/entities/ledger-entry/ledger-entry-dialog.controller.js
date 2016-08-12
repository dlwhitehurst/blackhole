(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .controller('LedgerEntryDialogController', LedgerEntryDialogController);

    LedgerEntryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'GenAccount','LedgerEntry'];

    function LedgerEntryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, GenAccount, LedgerEntry) {
        var vm = this;

        vm.ledgerEntry = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        $scope.genAccounts = GenAccount.query({size:200});

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.ledgerEntry.id !== null) {
                LedgerEntry.update(vm.ledgerEntry, onSaveSuccess, onSaveError);
            } else {
                LedgerEntry.save(vm.ledgerEntry, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('blackholeApp:ledgerEntryUpdate', result);
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
