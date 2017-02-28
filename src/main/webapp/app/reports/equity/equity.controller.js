(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .controller('EquityController', EquityController);
    EquityController.$inject = ['$scope', '$state', 'LastEntry'];

    function EquityController ($scope, $state, LastEntry) {
        var vm = this;
        $scope.lastEntry = LastEntry.get();
    }
})();
