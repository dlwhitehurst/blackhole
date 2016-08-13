(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .factory('IdeaSearch', IdeaSearch);

    IdeaSearch.$inject = ['$resource'];

    function IdeaSearch($resource) {
        var resourceUrl =  'api/_search/ideas/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
