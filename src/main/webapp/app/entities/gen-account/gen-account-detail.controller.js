(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .controller('GenAccountDetailController', GenAccountDetailController);

    GenAccountDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'GenAccount'];

    function GenAccountDetailController($scope, $rootScope, $stateParams, previousState, entity, GenAccount) {
        var vm = this;

        vm.genAccount = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('blackholeApp:genAccountUpdate', function(event, result) {
            vm.genAccount = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
