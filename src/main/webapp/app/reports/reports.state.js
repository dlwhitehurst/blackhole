(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('reports', {
            abstract: true,
            parent: 'app'
        });
    }
})();
