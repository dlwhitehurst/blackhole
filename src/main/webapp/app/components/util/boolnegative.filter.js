(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .filter('boolNegative', boolNegative);

    function boolNegative() {
        return boolNegativeFilter;

        function boolNegativeFilter (input) {
            var retVal = false;
            if (input.includes("(")) {
                retVal = true;
            }
            return retVal;
        }
    }
})();
