(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .controller('IncomeController', IncomeController);
    IncomeController.$inject = ['$scope', '$state', 'LastEntry', 'DebitTrialBalance', 'CreditTrialBalance'];

    function IncomeController ($scope, $state, LastEntry, DebitTrialBalance, CreditTrialBalance) {
        var vm = this;
        $scope.lastEntry = LastEntry.get();
        $scope.debitbalances = DebitTrialBalance.query({size:200});
        $scope.creditbalances = CreditTrialBalance.query({size:200});
    }
})();
