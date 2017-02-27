(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .controller('GenJournalDialogController', GenJournalDialogController);

    GenJournalDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'GenAccount','GenJournal'];

    function GenJournalDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, GenAccount, GenJournal) {
        var vm = this;

        vm.genJournal = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        $scope.genAccounts = GenAccount.query({size:200});
        
        $scope.GetDebitAccountSelect = function (genAccount) {
            var cno = vm.genJournal.dacctno;
            var name = $.grep($scope.genAccounts, function (genAccount) {
                return genAccount.cno == cno;
            })[0].name;
        	vm.genJournal.dacctno = cno;
        	vm.genJournal.debitaccountname = name;
        }        

        $scope.GetCreditAccountSelect = function (genAccount) {
            var cno = vm.genJournal.cacctno;
            var name = $.grep($scope.genAccounts, function (genAccount) {
                return genAccount.cno == cno;
            })[0].name;
        	vm.genJournal.cacctno = cno;
        	vm.genJournal.creditaccountname = name;
        }        
        
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.genJournal.id !== null) {
                GenJournal.update(vm.genJournal, onSaveSuccess, onSaveError);
            } else {
            	GenJournal.save(vm.genJournal, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('blackholeApp:genJournalUpdate', result);
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
