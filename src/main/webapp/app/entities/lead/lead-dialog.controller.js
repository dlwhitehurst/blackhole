(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .controller('LeadDialogController', LeadDialogController);

    LeadDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Lead'];

    function LeadDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Lead) {
        var vm = this;

        vm.lead = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.lead.id !== null) {
                Lead.update(vm.lead, onSaveSuccess, onSaveError);
            } else {
                Lead.save(vm.lead, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('blackholeApp:leadUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
