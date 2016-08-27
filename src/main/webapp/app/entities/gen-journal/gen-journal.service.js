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
                        data.entrydate = DateUtils.convertLocalDateFromServer(data.entrydate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.entrydate = DateUtils.convertLocalDateToServer(data.entrydate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.entrydate = DateUtils.convertLocalDateToServer(data.entrydate);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
