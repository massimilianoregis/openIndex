package org.index.test;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.index.obj.Category;
import org.index.obj.Item;
import org.index.obj.Pricing;
import org.index.obj.Shop;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import junit.framework.Assert;


public class IndexTest 
	{

	private ObjectMapper mapper = new ObjectMapper();
	public String toJson(Object obj)
		{
		try{
			return mapper.writeValueAsString(obj);
			}
		catch (Exception e) {
			e.printStackTrace();
			return "{}";
			}
		}

	/**		SHOP 	**/
	@Test
	public void deleteShops()
		{
		Shop[] list =new RestTemplate().getForObject("http://localhost:8080/index/shop", Shop[].class);
		for(Shop item:list)
			new RestTemplate().delete("http://localhost:8080/index/shop/"+item.getId());			
		}
	@Test 
	public void newShop()
		{
		new RestTemplate().postForLocation("http://localhost:8080/index/shop",	new Shop("starwars","Star wars"));
		Assert.assertEquals(true, true);
		}
	@Test
	public void shopList()
		{
		System.err.println("----------SHOP-------------");
		Shop[] list =new RestTemplate().getForObject("http://localhost:8080/index/shop", Shop[].class);
		System.err.println(toJson(list));
		}
	
	/**		CATEGORIE 	**/
	@Test
	public void addCategory()
		{
		System.err.println("----------CATEGORIE-------------");
		String shop =new RestTemplate().getForObject("http://localhost:8080/index/shop", Shop[].class)[0].getId();		
		new RestTemplate().postForLocation("http://localhost:8080/index/category",	new Category("oggetti",shop));
		}
	@Test
	public void categoryList()
		{
		String shop =new RestTemplate().getForObject("http://localhost:8080/index/shop", Shop[].class)[0].getId();
		
		Map map = new HashMap();
			map.put("shop",shop);
		Category[] list = new RestTemplate().getForObject("http://localhost:8080/index/category",	Category[].class);
		System.err.println(toJson(list));
		}
	
	/** PRICING **/
	@Test
	public void addPricing() throws Exception
		{
		System.err.println("----------PRICING-------------");
		String shop =new RestTemplate().getForObject("http://localhost:8080/index/shop", Shop[].class)[0].getId();
				
		new RestTemplate().postForLocation("http://localhost:8080/index/pricing",new Pricing("shop",shop));		
		new RestTemplate().postForLocation("http://localhost:8080/index/pricing",new Pricing("web",shop));		
		new RestTemplate().postForLocation("http://localhost:8080/index/pricing",new Pricing("bse",shop));
		}
	@Test
	public void getPricing() throws Exception
		{
		System.err.println("----------PRICING-------------");
		String shop =new RestTemplate().getForObject("http://localhost:8080/index/shop", Shop[].class)[0].getId();
				
		new RestTemplate().postForLocation("http://localhost:8080/index/pricing",new Pricing("shop",shop));		
		new RestTemplate().postForLocation("http://localhost:8080/index/pricing",new Pricing("web",shop));		
		new RestTemplate().postForLocation("http://localhost:8080/index/pricing",new Pricing("base",shop));
		}
	
	@Test
	public void addItem()
		{		
		System.err.println("----------ITEM-------------");
		String json="{'id':'A2','shop':'starwars','code':'SPDLSR','name':'Spada Laser','categories':['starwars.oggetti'],'prices':{'starwars.web':10.0,'starwars.base':15.0}}".replaceAll("'", "\"");				
		System.err.println(json);
		
		
		HttpHeaders headers = new HttpHeaders();
		  		headers.setContentType(MediaType.APPLICATION_JSON);
	  	HttpEntity<String> requestEntity = new HttpEntity<String>(json,headers);	  	
	  	
		new RestTemplate().postForLocation("http://localhost:8080/index/item",requestEntity);	  
		}
	/*
	@Test
	public void addWear()
		{		
		String json="{'id':'A2','code':'A2','name':'Maglia','gallery':[{'img':'https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcRjYmNQ_Esj1K5Xsc9eSctcehxWANwnmv5yL5YN45Mlum8U_8X32w'}],'categories':['alimentari'],'prices':{'shop':10,'web':10,'base':15},'taglie':['42','44']}".replaceAll("'", "\"");
				System.out.println(json);
		HttpHeaders headers = new HttpHeaders();
		  		headers.setContentType(MediaType.APPLICATION_JSON);
	  	HttpEntity<String> requestEntity = new HttpEntity<String>(json,headers);	  	
	  	
		new RestTemplate().postForLocation("http://localhost:8080/index/wear",requestEntity);	  
		}*/
	@Test
	public void getItems()
		{
		String list =new RestTemplate().getForObject("http://localhost:8080/index/item", String.class);		
		System.err.println(list);
		}
	
}
