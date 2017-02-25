(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .controller('GenAccountController', GenAccountController);

    GenAccountController.$inject = ['$scope', '$state', 'GenAccount'];

    function GenAccountController ($scope, $state, GenAccount) {
        var vm = this;
        $scope.genAccounts = GenAccount.query({size:200});        

    }
})();
