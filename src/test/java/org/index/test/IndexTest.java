package org.index.test;

import org.index.obj.Category;
import org.index.obj.Item;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;


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

	@Test
	public void addCategory()
		{
		
		
		new RestTemplate().postForLocation("http://localhost:8080/index/category",	new Category("alimentari"));
		}
	@Test
	public void addPricing()
		{
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			map.add("name", "shop");
		new RestTemplate().postForLocation("http://localhost:8080/index/pricing",map);
		map = new LinkedMultiValueMap<String, Object>();
			map.add("name", "web");
		new RestTemplate().postForLocation("http://localhost:8080/index/pricing",map);
		map = new LinkedMultiValueMap<String, Object>();
			map.add("name", "base");
		new RestTemplate().postForLocation("http://localhost:8080/index/pricing",map);
		}
	@Test
	public void addItem()
		{		
		String json="{'id':'A3','code':'A3','name':'Panino','gallery':[{'img':'https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcTJQ6B8Q-uk1qQn5H3c9uQS9yZ9jaRu6iALbfdbusIp-65CLD-f'}],'categories':['alimentari'],'prices':{'shop':10,'web':10,'base':15}}".replaceAll("'", "\"");
				System.out.println(json);
		HttpHeaders headers = new HttpHeaders();
		  		headers.setContentType(MediaType.APPLICATION_JSON);
	  	HttpEntity<String> requestEntity = new HttpEntity<String>(json,headers);	  	
	  	
		new RestTemplate().postForLocation("http://localhost:8080/index/item",requestEntity);	  
		}
	@Test
	public void addWear()
		{		
		String json="{'id':'A2','code':'A2','name':'Maglia','gallery':[{'img':'https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcRjYmNQ_Esj1K5Xsc9eSctcehxWANwnmv5yL5YN45Mlum8U_8X32w'}],'categories':['alimentari'],'prices':{'shop':10,'web':10,'base':15},'taglie':['42','44']}".replaceAll("'", "\"");
				System.out.println(json);
		HttpHeaders headers = new HttpHeaders();
		  		headers.setContentType(MediaType.APPLICATION_JSON);
	  	HttpEntity<String> requestEntity = new HttpEntity<String>(json,headers);	  	
	  	
		new RestTemplate().postForLocation("http://localhost:8080/index/wear",requestEntity);	  
		}
	@Test
	public void getItems()
		{
		Item[] list =new RestTemplate().getForObject("http://localhost:8080/index/item", Item[].class);		
		System.out.println(toJson(list));
		}
	
}
