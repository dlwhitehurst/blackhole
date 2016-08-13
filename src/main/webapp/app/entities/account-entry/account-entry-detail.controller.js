(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .controller('AccountEntryDetailController', AccountEntryDetailController);

    AccountEntryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AccountEntry'];

    function AccountEntryDetailController($scope, $rootScope, $stateParams, previousState, entity, AccountEntry) {
        var vm = this;

        vm.accountEntry = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('blackholeApp:accountEntryUpdate', function(event, result) {
            vm.accountEntry = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
