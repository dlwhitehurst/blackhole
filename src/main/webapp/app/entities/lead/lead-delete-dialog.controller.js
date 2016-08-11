(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .controller('LeadDeleteController',LeadDeleteController);

    LeadDeleteController.$inject = ['$uibModalInstance', 'entity', 'Lead'];

    function LeadDeleteController($uibModalInstance, entity, Lead) {
        var vm = this;

        vm.lead = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Lead.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
