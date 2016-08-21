(function() {
    'use strict';
    angular
        .module('blackholeApp')
        .factory('DebitFoot', DebitFoot);

    DebitFoot.$inject = ['$resource'];

    function DebitFoot ($resource) {
        var resourceUrl =  'api/debitbalances/foot';

        return $resource(resourceUrl, {}, {
          'get': { method: 'GET', isArray: false}
        });
    }
})();
