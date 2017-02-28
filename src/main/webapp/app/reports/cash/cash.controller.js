(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .controller('CashController', CashController);
    CashController.$inject = ['$scope', '$state', 'LastEntry'];

    function CashController ($scope, $state, LastEntry) {
        var vm = this;
        $scope.lastEntry = LastEntry.get();
    }
})();
