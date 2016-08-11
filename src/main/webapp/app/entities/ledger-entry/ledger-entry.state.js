(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('ledger-entry', {
            parent: 'entity',
            url: '/ledger-entry?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'blackholeApp.ledgerEntry.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ledger-entry/ledger-entries.html',
                    controller: 'LedgerEntryController',
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
                    $translatePartialLoader.addPart('ledgerEntry');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('ledger-entry-detail', {
            parent: 'entity',
            url: '/ledger-entry/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'blackholeApp.ledgerEntry.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ledger-entry/ledger-entry-detail.html',
                    controller: 'LedgerEntryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('ledgerEntry');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'LedgerEntry', function($stateParams, LedgerEntry) {
                    return LedgerEntry.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'ledger-entry',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('ledger-entry-detail.edit', {
            parent: 'ledger-entry-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ledger-entry/ledger-entry-dialog.html',
                    controller: 'LedgerEntryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LedgerEntry', function(LedgerEntry) {
                            return LedgerEntry.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('ledger-entry.new', {
            parent: 'ledger-entry',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ledger-entry/ledger-entry-dialog.html',
                    controller: 'LedgerEntryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                entrydate: null,
                                transaction: null,
                                dacctno: null,
                                cacctno: null,
                                dadebit: null,
                                dacredit: null,
                                cadebit: null,
                                cacredit: null,
                                notes: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('ledger-entry', null, { reload: true });
                }, function() {
                    $state.go('ledger-entry');
                });
            }]
        })
        .state('ledger-entry.edit', {
            parent: 'ledger-entry',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ledger-entry/ledger-entry-dialog.html',
                    controller: 'LedgerEntryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LedgerEntry', function(LedgerEntry) {
                            return LedgerEntry.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('ledger-entry', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('ledger-entry.delete', {
            parent: 'ledger-entry',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ledger-entry/ledger-entry-delete-dialog.html',
                    controller: 'LedgerEntryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['LedgerEntry', function(LedgerEntry) {
                            return LedgerEntry.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('ledger-entry', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
