(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('gen-account', {
            parent: 'entity',
            url: '/gen-account?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'blackholeApp.genAccount.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/gen-account/gen-accounts.html',
                    controller: 'GenAccountController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('genAccount');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('gen-account-detail', {
            parent: 'entity',
            url: '/gen-account/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'blackholeApp.genAccount.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/gen-account/gen-account-detail.html',
                    controller: 'GenAccountDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('genAccount');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'GenAccount', function($stateParams, GenAccount) {
                    return GenAccount.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'gen-account',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('gen-account-detail.edit', {
            parent: 'gen-account-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/gen-account/gen-account-dialog.html',
                    controller: 'GenAccountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['GenAccount', function(GenAccount) {
                            return GenAccount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('gen-account.new', {
            parent: 'gen-account',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/gen-account/gen-account-dialog.html',
                    controller: 'GenAccountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                type: null,
                                dc: null,
                                cno: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('gen-account', null, { reload: true });
                }, function() {
                    $state.go('gen-account');
                });
            }]
        })
        .state('gen-account.edit', {
            parent: 'gen-account',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/gen-account/gen-account-dialog.html',
                    controller: 'GenAccountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['GenAccount', function(GenAccount) {
                            return GenAccount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('gen-account', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('gen-account.delete', {
            parent: 'gen-account',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/gen-account/gen-account-delete-dialog.html',
                    controller: 'GenAccountDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['GenAccount', function(GenAccount) {
                            return GenAccount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('gen-account', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
