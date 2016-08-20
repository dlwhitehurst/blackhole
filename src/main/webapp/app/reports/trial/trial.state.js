(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('trial', {
            parent: 'reports',
            url: '/trial',
            data: {
              authorities: ['ROLE_USER'],
              pageTitle: 'blackholeApp.trial.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/reports/trial/trial.html',
                    controller: 'TrialController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('trial');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
