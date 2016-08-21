(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .filter('trialBalance', trialBalance);

    function trialBalance() {
      return trialBalanceFilter;

      function trialBalanceFilter (input, cno) {
        for(var i = 0; i < input.length; i++) {
          if(input[i].cno === cno) {
            return input[i].balance;
          }
        }
      }
    }
})();
