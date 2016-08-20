(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('cash', {
            parent: 'reports',
            url: '/cash',
            data: {
              authorities: ['ROLE_USER'],
              pageTitle: 'blackholeApp.cash.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/reports/cash/cash.html',
                    controller: 'CashController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('cash');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
