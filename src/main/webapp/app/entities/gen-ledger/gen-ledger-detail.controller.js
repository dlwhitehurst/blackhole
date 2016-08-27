(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .controller('GenLedgerDetailController', GenLedgerDetailController);

    GenLedgerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'GenLedger'];

    function GenLedgerDetailController($scope, $rootScope, $stateParams, previousState, entity, GenLedger) {
        var vm = this;

        vm.genLedger = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('blackholeApp:genLedgerUpdate', function(event, result) {
            vm.genLedger = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
