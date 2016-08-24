(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .filter('mycurrency', mycurrency);

    function mycurrency() {
        return mycurrencyFilter;

        function mycurrencyFilter (input) {
          if (input !== null) {
            if (input.includes("-")) {
              input = input.replace("-","");
              input = '$(' + input + ')';
            } else {
              input = '$' + input;
            }
            return input;
          } else {
            return '';
          }
        }
    }
})();
