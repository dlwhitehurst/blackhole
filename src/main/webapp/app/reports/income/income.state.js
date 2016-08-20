(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('income', {
            parent: 'reports',
            url: '/income',
            data: {
              authorities: ['ROLE_USER'],
              pageTitle: 'blackholeApp.income.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/reports/income/income.html',
                    controller: 'IncomeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('income');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
