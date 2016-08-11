(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .factory('LedgerEntrySearch', LedgerEntrySearch);

    LedgerEntrySearch.$inject = ['$resource'];

    function LedgerEntrySearch($resource) {
        var resourceUrl =  'api/_search/ledger-entries/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
