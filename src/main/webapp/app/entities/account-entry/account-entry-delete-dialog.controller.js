(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .controller('AccountEntryDeleteController',AccountEntryDeleteController);

    AccountEntryDeleteController.$inject = ['$uibModalInstance', 'entity', 'AccountEntry'];

    function AccountEntryDeleteController($uibModalInstance, entity, AccountEntry) {
        var vm = this;

        vm.accountEntry = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AccountEntry.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
