(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .controller('TrialController', TrialController);
    TrialController.$inject = ['$scope', '$state'];

    function TrialController ($scope, $state) {
        var vm = this;
    }
})();
