(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .factory('LeadSearch', LeadSearch);

    LeadSearch.$inject = ['$resource'];

    function LeadSearch($resource) {
        var resourceUrl =  'api/_search/leads/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
