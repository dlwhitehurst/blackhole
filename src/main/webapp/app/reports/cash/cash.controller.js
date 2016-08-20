(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .controller('CashController', CashController);
    CashController.$inject = ['$scope', '$state'];

    function CashController ($scope, $state) {
        var vm = this;
    }
})();
