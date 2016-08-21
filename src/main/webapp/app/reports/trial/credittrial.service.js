(function() {
    'use strict';
    angular
        .module('blackholeApp')
        .factory('CreditTrialBalance', CreditTrialBalance);

    CreditTrialBalance.$inject = ['$resource'];

    function CreditTrialBalance ($resource) {
        var resourceUrl =  'api/creditbalances';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
