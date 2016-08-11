(function() {
    'use strict';
    angular
        .module('blackholeApp')
        .factory('Lead', Lead);

    Lead.$inject = ['$resource'];

    function Lead ($resource) {
        var resourceUrl =  'api/leads/:id';

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
