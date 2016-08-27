(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .controller('GenJournalDetailController', GenJournalDetailController);

    GenJournalDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'GenJournal'];

    function GenJournalDetailController($scope, $rootScope, $stateParams, previousState, entity, GenJournal) {
        var vm = this;

        vm.genJournal = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('blackholeApp:genJournalUpdate', function(event, result) {
            vm.genJournal = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
