(function() {
    'use strict';
    angular
        .module('blackholeApp')
        .factory('CreditFoot', CreditFoot);

    CreditFoot.$inject = ['$resource'];

    function CreditFoot ($resource) {
        var resourceUrl =  'api/creditbalances/foot';

        return $resource(resourceUrl, {}, {
          'get': { method: 'GET', isArray: false}
        });
    }
})();
