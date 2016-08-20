(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .controller('IncomeController', IncomeController);
    IncomeController.$inject = ['$scope', '$state'];

    function IncomeController ($scope, $state) {
        var vm = this;
    }
})();
