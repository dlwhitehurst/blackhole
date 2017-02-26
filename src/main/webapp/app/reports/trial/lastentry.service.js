(function() {
    'use strict';
    angular
        .module('blackholeApp')
        .factory('LastEntry', LastEntry);

    LastEntry.$inject = ['$resource', 'DateUtils'];

    function LastEntry ($resource, $DateUtils) {
        var resourceUrl =  'api/lastentry';

        return $resource(resourceUrl, {}, {
        	'get': { method: 'GET', isArray: false}        
        });
    }
})();
