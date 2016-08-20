(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('equity', {
            parent: 'reports',
            url: '/equity',
            data: {
              authorities: ['ROLE_USER'],
              pageTitle: 'blackholeApp.equity.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/reports/equity/equity.html',
                    controller: 'EquityController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('equity');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
