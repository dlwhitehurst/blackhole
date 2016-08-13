(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('idea', {
            parent: 'entity',
            url: '/idea?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'blackholeApp.idea.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/idea/ideas.html',
                    controller: 'IdeaController',
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
                    $translatePartialLoader.addPart('idea');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('idea-detail', {
            parent: 'entity',
            url: '/idea/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'blackholeApp.idea.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/idea/idea-detail.html',
                    controller: 'IdeaDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('idea');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Idea', function($stateParams, Idea) {
                    return Idea.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'idea',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('idea-detail.edit', {
            parent: 'idea-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/idea/idea-dialog.html',
                    controller: 'IdeaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Idea', function(Idea) {
                            return Idea.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('idea.new', {
            parent: 'idea',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/idea/idea-dialog.html',
                    controller: 'IdeaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                entrydate: null,
                                idea: null,
                                inprocess: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('idea', null, { reload: true });
                }, function() {
                    $state.go('idea');
                });
            }]
        })
        .state('idea.edit', {
            parent: 'idea',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/idea/idea-dialog.html',
                    controller: 'IdeaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Idea', function(Idea) {
                            return Idea.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('idea', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('idea.delete', {
            parent: 'idea',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/idea/idea-delete-dialog.html',
                    controller: 'IdeaDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Idea', function(Idea) {
                            return Idea.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('idea', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
