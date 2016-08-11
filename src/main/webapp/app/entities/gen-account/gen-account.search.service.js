(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .factory('GenAccountSearch', GenAccountSearch);

    GenAccountSearch.$inject = ['$resource'];

    function GenAccountSearch($resource) {
        var resourceUrl =  'api/_search/gen-accounts/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
