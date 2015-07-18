angular.module("vending",["ngAnimate","ngResource","ngRoute","keyboard"])
.constant("url","//localhost:8080/angular/data")
.config(['$routeProvider', function($routeProvider) 
     		{
	$routeProvider    		    		    			    		    	
		.when('/', {templateUrl: 'home.html'})
		.when('/shop', {templateUrl: 'product.html',controller:"shopController"})
		    		
		.when('/404',{templateUrl: 'views/pages/404.html'})
		.otherwise({redirectTo: '/products/list'});
	}])
.factory("user",["$resource","url",function($resource,url)
	{
	return $resource(url+"/user/:fiscal");
	}])
.factory("product",["$resource","url",function($resource,url)
	{
	return $resource(url+"/product/:number");
	}])
.factory("drop",["$resource",function($resource)
	{
	return $resource("/drop/:number");
	}])
/*
.factory("keyboardProduct",["shop",function(shop)
    {
	var result=
		{
		code:"",
		scope:null
		};
	
	angular.element(document).on("keypress",function(e) 
		{				
	 	var char = parseInt(String.fromCharCode(e.which));			 	
	 	if(isNaN(char)) result.code="";
	 	else result.code+=char;
	 	result.scope.$apply();
	 	
	 	if(result.code.length==3)
	 		{
	 		shop.select(result.code)
	 		result.code="";
	 		}
		});
	return result;
    }])*/
.factory("shop",["user","product","$timeout","$location",function(user,product,$timeout,$location)
    {	
	return {					
		difference:null,
		position:'',
		select:function(position)
			{						
			if(position!=null && position.length!=3) return;			
			if($location.url()!="/shop") $location.url("/shop");
			var act = this;
			this.position=position;
			
			product.get({number:position},function(data)
				{
				
				act.product=data;
				})
			},	
		exit:function()
			{		
			var act=this;
			act.user.logged=false;			
			$timeout(function()
				{
				act.user={balance:0};
				//act.product=null;
				},1000);			
			},
		enter:function(fiscalCode)
			{				
			var act = this;
			user.get({fiscal:fiscalCode},function(data)
				{			
				data.balance+=act.user.balance;
				act.user=data;
				act.user.logged=true;				
				})
			},
		calcDifference:function()
			{			
			if(this.product==null) this.difference= null;
			else
				try			{this.difference= this.user.balance-this.product.price;}
				catch(e)	{this.difference= -this.product.price;}
			return this.difference;
			},
		buy:function()	
			{					
			if(this.difference>=0) 
				this.user.balance-=this.product.price;			
			}
		}
    }])
.controller("productsController",["$scope","product","$timeout",function($scope,product,$timeout)
    {
	$scope.products = 
		[
		 {position:"111",name:"prodotto1",price:100},
		 {position:"113",name:"prodotto2",price:100},
		 {position:"116",name:"prodotto3",price:100},
		 {position:"118",name:"prodotto4",price:100}
		]; 
    }])
.controller("shopController",["$http","$scope","shop","$timeout","keyboard",function($http,$scope,shop,$timeout,keyboard)
	{				
	$scope.keyboard=keyboard;
	$scope.position="111";
	$scope.shop=shop;
	$scope.shop.user={balance:0};
	$scope.addMoney=function(value)
		{
		shop.user.balance+=value;
		}
	$scope.$watch(function(){$scope.shop.calcDifference()});
	$scope.$watch("position",function(data){$scope.shop.select(data);})
	$scope.$watch("client",function(data){if(data.cf!="") $scope.shop.enter(data.cf); else $scope.shop.exit()})
	
	$scope.clienti=
		[		
		{cf:""},
		{cf:"RGSMSM77M06A859I"},
		{cf:"MNSMHL78S63A859W"}
		]
	}])
.run(["$rootScope","keyboard",function($rootScope,keyboard)
      {	
	  
		root = $rootScope;
		//keyboardProduct.scope=root;
		//root.keyboard=keyboardProduct;	
      }]);
