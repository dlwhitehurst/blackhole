(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .controller('GenLedgerDeleteController',GenLedgerDeleteController);

    GenLedgerDeleteController.$inject = ['$uibModalInstance', 'entity', 'GenLedger'];

    function GenLedgerDeleteController($uibModalInstance, entity, GenLedger) {
        var vm = this;

        vm.genLedger = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            GenLedger.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
