angular.module('vending',["ngRoute"])
  .config(['$routeProvider', function($routeProvider) 
  		{
    	$routeProvider    		    		    			    		    	
    		.when('/', {templateUrl: 'home.html'})
    		.when('/shop', {templateUrl: 'product.html',controller:"shopController"})
    		    		
    		.when('/404',{templateUrl: 'views/pages/404.html'})
    		.otherwise({redirectTo: '/products/list'});
    	}])