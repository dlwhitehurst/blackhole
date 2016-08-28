(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('gen-ledger', {
            parent: 'entity',
            url: '/gen-ledger?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'blackholeApp.genLedger.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/gen-ledger/gen-ledger-entries.html',
                    controller: 'GenLedgerController',
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
                    $translatePartialLoader.addPart('genLedger');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('gen-ledger-detail', {
            parent: 'entity',
            url: '/gen-ledger/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'blackholeApp.genLedger.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/gen-ledger/gen-ledger-detail.html',
                    controller: 'GenLedgerDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('genLedger');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'GenLedger', function($stateParams, GenLedger) {
                    return GenLedger.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'gen-ledger',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('gen-ledger-detail.edit', {
            parent: 'gen-ledger-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/gen-ledger/gen-ledger-dialog.html',
                    controller: 'GenLedgerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['GenLedger', function(GenLedger) {
                            return GenLedger.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('gen-ledger.new', {
            parent: 'gen-ledger',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/gen-ledger/gen-ledger-dialog.html',
                    controller: 'GenLedgerDialogController',
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
                                accountName: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('gen-ledger', null, { reload: true });
                }, function() {
                    $state.go('gen-ledger');
                });
            }]
        })
        .state('gen-ledger.edit', {
            parent: 'gen-ledger',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/gen-ledger/gen-ledger-dialog.html',
                    controller: 'GenLedgerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['GenLedger', function(GenLedger) {
                            return GenLedger.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('gen-ledger', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('gen-ledger.delete', {
            parent: 'gen-ledger',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/gen-ledger/gen-ledger-delete-dialog.html',
                    controller: 'GenLedgerDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['GenLedger', function(GenLedger) {
                            return GenLedger.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('gen-ledger', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
