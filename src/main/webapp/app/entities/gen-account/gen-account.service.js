(function() {
    'use strict';
    angular
        .module('blackholeApp')
        .factory('GenAccount', GenAccount);

    GenAccount.$inject = ['$resource'];

    function GenAccount ($resource) {
        var resourceUrl =  'api/gen-accounts/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
