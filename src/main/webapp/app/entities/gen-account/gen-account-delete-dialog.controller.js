(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .controller('GenAccountDeleteController',GenAccountDeleteController);

    GenAccountDeleteController.$inject = ['$uibModalInstance', 'entity', 'GenAccount'];

    function GenAccountDeleteController($uibModalInstance, entity, GenAccount) {
        var vm = this;

        vm.genAccount = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            GenAccount.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
