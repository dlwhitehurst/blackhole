(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .controller('GenJournalDeleteController',GenJournalDeleteController);

    GenJournalDeleteController.$inject = ['$uibModalInstance', 'entity', 'GenJournal'];

    function GenJournalDeleteController($uibModalInstance, entity, GenJournal) {
        var vm = this;

        vm.genJournal = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            GenJournal.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
