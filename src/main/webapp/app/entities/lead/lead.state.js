(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('lead', {
            parent: 'entity',
            url: '/lead?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'blackholeApp.lead.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lead/leads.html',
                    controller: 'LeadController',
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
                    $translatePartialLoader.addPart('lead');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('lead-detail', {
            parent: 'entity',
            url: '/lead/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'blackholeApp.lead.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lead/lead-detail.html',
                    controller: 'LeadDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('lead');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Lead', function($stateParams, Lead) {
                    return Lead.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'lead',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('lead-detail.edit', {
            parent: 'lead-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lead/lead-dialog.html',
                    controller: 'LeadDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Lead', function(Lead) {
                            return Lead.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('lead.new', {
            parent: 'lead',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lead/lead-dialog.html',
                    controller: 'LeadDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                referringco: null,
                                referringname: null,
                                email: null,
                                phone: null,
                                opp: null,
                                oppwhere: null,
                                oppwho: null,
                                notes: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('lead', null, { reload: true });
                }, function() {
                    $state.go('lead');
                });
            }]
        })
        .state('lead.edit', {
            parent: 'lead',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lead/lead-dialog.html',
                    controller: 'LeadDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Lead', function(Lead) {
                            return Lead.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('lead', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('lead.delete', {
            parent: 'lead',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lead/lead-delete-dialog.html',
                    controller: 'LeadDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Lead', function(Lead) {
                            return Lead.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('lead', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
