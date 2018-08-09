'use strict';

/**
 * @ngdoc function
 * @name testAppApp.controller:TestCtrl
 * @description
 * # TestCtrl
 * Controller of the testAppApp
 */
define(['angular'], function (angular) {
	angular.module('app')
	  .controller('TestCtrl', function () {
	    this.awesomeThings = [
	      'HTML5 Boilerplate',
	      'AngularJS',
	      'Karma'
	    ];
	  });
	
});

