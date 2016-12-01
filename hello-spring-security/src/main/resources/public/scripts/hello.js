'use strict';

//declare modules
angular.module('Authentication', []);
angular.module('Home', []);

angular.module('Demo', [
	'Authentication',
	'Home',
	'ngRoute',
	'ngCookies',
	'ngMaterial'
	])

	.config(['$routeProvider', function ($routeProvider) {

		console.log("config");
		$routeProvider
		.when('/basic/login', {
			controller: 'LoginController',
			templateUrl: 'modules/authentication/views/login.html'
		})
		.when('/digest/login', {
			controller: 'LoginController',
			templateUrl: 'modules/authentication/views/login.html'
		})
		.when('/home', {
			controller: 'HomeController',
			templateUrl: 'modules/home/views/home.html'
		})
		.otherwise({ redirectTo: '/' });
	}])

	.run(['$rootScope', '$location', '$cookieStore', '$http', 
		function ($rootScope, $location, $cookieStore, $http) {
		console.log("run");
		console.log($rootScope);
		// keep user logged in after page refresh
		$rootScope.globals = $cookieStore.get('globals') || {};
		if ($rootScope.globals.currentUser) {
			$http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.currentUser.authdata; // jshint ignore:line
		}

		$rootScope.$on('$locationChangeStart', function (event, next, current) {
			// redirect to login page if not logged in
			if (!$rootScope.globals.currentUser && $rootScope.currentNavItem == 'basic') {
				$location.path('/basic/login');
			}
		});
	}])

	.controller('AppCtrl', function ($scope) {
		$scope.currentNavItem = 'basic';
	});