(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .controller('LedgerEntryDeleteController',LedgerEntryDeleteController);

    LedgerEntryDeleteController.$inject = ['$uibModalInstance', 'entity', 'LedgerEntry'];

    function LedgerEntryDeleteController($uibModalInstance, entity, LedgerEntry) {
        var vm = this;

        vm.ledgerEntry = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            LedgerEntry.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
