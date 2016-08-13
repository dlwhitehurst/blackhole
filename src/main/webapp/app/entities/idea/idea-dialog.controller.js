(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .controller('IdeaDialogController', IdeaDialogController);

    IdeaDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Idea'];

    function IdeaDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Idea) {
        var vm = this;

        vm.idea = entity;
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
            if (vm.idea.id !== null) {
                Idea.update(vm.idea, onSaveSuccess, onSaveError);
            } else {
                Idea.save(vm.idea, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('blackholeApp:ideaUpdate', result);
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
