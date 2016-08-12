(function() {
    'use strict';

    angular
        .module('blackholeApp')
        .filter('chartName', chartName);

    function chartName() {
      return chartNameFilter;

      function chartNameFilter (input, cno) {
        for(var i = 0; i < input.length; i++) {
          if(input[i].cno === cno) {
            return input[i].name;
          }
        }
      }
    }
})();
