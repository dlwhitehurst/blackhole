(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('account-entry', {
            parent: 'entity',
            url: '/account-entry?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'blackholeApp.accountEntry.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/account-entry/account-entries.html',
                    controller: 'AccountEntryController',
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
                    $translatePartialLoader.addPart('accountEntry');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('account-entry-detail', {
            parent: 'entity',
            url: '/account-entry/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'blackholeApp.accountEntry.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/account-entry/account-entry-detail.html',
                    controller: 'AccountEntryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('accountEntry');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'AccountEntry', function($stateParams, AccountEntry) {
                    return AccountEntry.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'account-entry',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('account-entry-detail.edit', {
            parent: 'account-entry-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/account-entry/account-entry-dialog.html',
                    controller: 'AccountEntryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AccountEntry', function(AccountEntry) {
                            return AccountEntry.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('account-entry.new', {
            parent: 'account-entry',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/account-entry/account-entry-dialog.html',
                    controller: 'AccountEntryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                entrydate: null,
                                transaction: null,
                                reconcile: null,
                                postingref: null,
                                debit: null,
                                credit: null,
                                debitbalance: null,
                                creditbalance: null,
                                notes: null,
                                cno: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('account-entry', null, { reload: true });
                }, function() {
                    $state.go('account-entry');
                });
            }]
        })
        .state('account-entry.edit', {
            parent: 'account-entry',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/account-entry/account-entry-dialog.html',
                    controller: 'AccountEntryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AccountEntry', function(AccountEntry) {
                            return AccountEntry.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('account-entry', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('account-entry.delete', {
            parent: 'account-entry',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/account-entry/account-entry-delete-dialog.html',
                    controller: 'AccountEntryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AccountEntry', function(AccountEntry) {
                            return AccountEntry.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('account-entry', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
