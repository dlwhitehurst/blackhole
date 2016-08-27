(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .factory('GenLedgerSearch', GenLedgerSearch);

    GenLedgerSearch.$inject = ['$resource'];

    function GenLedgerSearch($resource) {
        var resourceUrl =  'api/_search/gen-ledger-entries/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
