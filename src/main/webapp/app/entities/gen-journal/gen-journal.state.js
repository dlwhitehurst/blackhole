(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('gen-journal', {
            parent: 'entity',
            url: '/gen-journal?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'blackholeApp.genJournal.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/gen-journal/gen-journal-entries.html',
                    controller: 'GenJournalController',
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
                    $translatePartialLoader.addPart('genJournal');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('gen-journal-detail', {
            parent: 'entity',
            url: '/gen-journal/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'blackholeApp.genJournal.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/gen-journal/gen-journal-detail.html',
                    controller: 'GenJournalDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('genJournal');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'GenJournal', function($stateParams, GenJournal) {
                    return GenJournal.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'gen-journal',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('gen-journal-detail.edit', {
            parent: 'gen-journal-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/gen-journal/gen-journal-dialog.html',
                    controller: 'GenJournalDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['GenJournal', function(GenJournal) {
                            return GenJournal.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('gen-journal.new', {
            parent: 'gen-journal',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/gen-journal/gen-journal-dialog.html',
                    controller: 'GenJournalDialogController',
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
                                debitaccountname: null,
                                creditaccountname: null,
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
                    $state.go('gen-journal', null, { reload: true });
                }, function() {
                    $state.go('gen-journal');
                });
            }]
        })
        .state('gen-journal.edit', {
            parent: 'gen-journal',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/gen-journal/gen-journal-dialog.html',
                    controller: 'GenJournalDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['GenJournal', function(GenJournal) {
                            return GenJournal.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('gen-journal', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('gen-journal.delete', {
            parent: 'gen-journal',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/gen-journal/gen-journal-delete-dialog.html',
                    controller: 'GenJournalDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['GenJournal', function(GenJournal) {
                            return GenJournal.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('gen-journal', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
