(function() {
    'use strict';
    angular
        .module('blackholeApp')
        .factory('DebitTrialBalance', DebitTrialBalance);

    DebitTrialBalance.$inject = ['$resource'];

    function DebitTrialBalance ($resource) {
        var resourceUrl =  'api/debitbalances';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
