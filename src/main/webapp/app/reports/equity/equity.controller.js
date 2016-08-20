(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .controller('EquityController', EquityController);
    EquityController.$inject = ['$scope', '$state'];

    function EquityController ($scope, $state) {
        var vm = this;
    }
})();
