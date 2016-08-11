(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .controller('LedgerEntryDetailController', LedgerEntryDetailController);

    LedgerEntryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'LedgerEntry'];

    function LedgerEntryDetailController($scope, $rootScope, $stateParams, previousState, entity, LedgerEntry) {
        var vm = this;

        vm.ledgerEntry = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('blackholeApp:ledgerEntryUpdate', function(event, result) {
            vm.ledgerEntry = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
