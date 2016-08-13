(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .factory('AccountEntrySearch', AccountEntrySearch);

    AccountEntrySearch.$inject = ['$resource'];

    function AccountEntrySearch($resource) {
        var resourceUrl =  'api/_search/account-entries/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
