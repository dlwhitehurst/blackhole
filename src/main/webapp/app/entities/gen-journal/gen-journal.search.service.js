(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .factory('GenJournalSearch', GenJournalSearch);

    GenJournalSearch.$inject = ['$resource'];

    function GenJournalSearch($resource) {
        var resourceUrl =  'api/_search/gen-journal-entries/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
