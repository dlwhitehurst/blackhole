(function() {
    'use strict';
    angular
        .module('blackholeApp')
        .factory('GenJournal', GenJournal);

    GenJournal.$inject = ['$resource', 'DateUtils'];

    function GenJournal ($resource, DateUtils) {
        var resourceUrl =  'api/gen-journal-entries/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.entrydate = DateUtils.convertDateTimeFromServer(data.entrydate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
