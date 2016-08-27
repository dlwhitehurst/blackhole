(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .controller('GenLedgerDialogController', GenLedgerDialogController);

    GenLedgerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'GenLedger'];

    function GenLedgerDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, GenLedger) {
        var vm = this;

        vm.genLedger = entity;
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
            if (vm.genLedger.id !== null) {
                GenLedger.update(vm.genLedger, onSaveSuccess, onSaveError);
            } else {
                GenLedger.save(vm.genLedger, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('blackholeApp:genLedgerUpdate', result);
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
