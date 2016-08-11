(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .controller('LeadDetailController', LeadDetailController);

    LeadDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Lead'];

    function LeadDetailController($scope, $rootScope, $stateParams, previousState, entity, Lead) {
        var vm = this;

        vm.lead = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('blackholeApp:leadUpdate', function(event, result) {
            vm.lead = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
