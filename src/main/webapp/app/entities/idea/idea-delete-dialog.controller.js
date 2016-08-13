(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .controller('IdeaDeleteController',IdeaDeleteController);

    IdeaDeleteController.$inject = ['$uibModalInstance', 'entity', 'Idea'];

    function IdeaDeleteController($uibModalInstance, entity, Idea) {
        var vm = this;

        vm.idea = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Idea.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
