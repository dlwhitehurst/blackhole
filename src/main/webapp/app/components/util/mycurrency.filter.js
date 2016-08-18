(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .filter('mycurrency', mycurrency);

    function mycurrency() {
        return mycurrencyFilter;

        function mycurrencyFilter (input) {
            if (input === null) {
              return;
            }
            if (input !== null) {
                input = '$' + input;
            }
            return input;
        }
    }
})();
