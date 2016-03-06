package org.opencommunity.security;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.index.obj.Shop;
import org.index.repository.Repositories;
import org.opencommunity.objs.User;
import org.opencommunity.util.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@PropertySource({"application.properties"})
public class AccessControl {
static public AccessControl instance;

private HttpServletRequest request;
private Client client;

@Value("${communityJwt}") 		private String communityJwt;

@Autowired
public AccessControl(HttpServletRequest request, Client client)
	{		
	System.out.println("*********************");
	System.out.println(request);
	System.out.println(client);
	this.request=request;
	AccessControl.instance=this;
	this.client=client;
	}


static public org.opencommunity.objs.User getUser()
	{
	Boolean userRead = (Boolean)instance.request.getAttribute("userRead");
	org.opencommunity.objs.User usr = (org.opencommunity.objs.User)instance.request.getAttribute("user");
	if(userRead!=null && userRead) return usr;	
			
	try {
		String jwt = instance.request.getHeader("access-token");
		usr=null;
		if(jwt!=null)
			{
			usr = (org.opencommunity.objs.User)instance.client.getForObject(instance.communityJwt,org.opencommunity.objs.User.class,jwt);
			System.out.println(new ObjectMapper().writeValueAsString(usr));
			if(usr.getMail()==null) usr=null;
			}
		
		return usr;
		}
	catch (Exception e) 
		{
		return usr;
		}
	finally
		{
		instance.request.setAttribute("userRead",true);
		instance.request.setAttribute("user",usr);
		}
	}
static public boolean isAdmin()
	{
	try	{
		return getUser().canAccess("admin",null);
		}
	catch(Exception e)
		{
		return false;
		}
	}
static public boolean isAdmin(String shop)
	{
	Boolean sh = (Boolean)instance.request.getAttribute(shop);
	if(sh!=null) return sh;
	
	sh=false;
	try {		
		User user = getUser();
		if(user==null) return sh;			
			
		List<Shop> shs = Repositories.shop.findByStaffMail(user.getMail());		
		for(Shop item:shs)
			if(item.getId().equals(shop))
				{
				sh=true;
				return sh;
				};
		
		return sh;
		}
	catch(Exception e)
		{
		return sh;
		}
	finally 
		{
		instance.request.setAttribute(shop,sh);
		}
	}
static public boolean canAccess(String role)
	{
	String company= null;
	try	{
		String[] part = role.split(".");
		company=part[0];
		role=part[1];
		}
	catch(Exception e){}
	
	try{
		return getUser().canAccess(role,company);
		}
	catch (Exception e) {
		return false;
		}
	}
static public boolean canAccess(String role,String company)
	{
	try{
		return getUser().canAccess(role,company);
		}
	catch (Exception e) {
		return false;
		}
	}
/*
static public boolean canAccess(String role,String company)
	{
	System.out.println("access Control?");
	try{
	return instance.getUser().canAccess(role, company);
	}catch (Exception e) {
	e.printStackTrace();
	return false;
	}
	}*/

public static void main(String[] args) {
	String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJtYWlsIjoibWFzc2ltaWxpYW5vLnJlZ2lzQGVrYXJvcy5pdCIsInBzdyI6bnVsbCwiZmlyc3ROYW1lIjoiTWFzc2ltaWxpYW5vIiwibGFzdE5hbWUiOiJSZWdpcyIsImxhc3RhY2Nlc3N0aW1lIjpudWxsLCJzZW5kUmVnaXN0ZXJNYWlsIjpudWxsLCJsb2d0cnkiOjAsInJlZ2lzdGVySWQiOm51bGwsInJvbGVzIjpbeyJpZCI6ImJhc2UudXNlciIsIm5hbWUiOiJ1c2VyIiwiY29tcGFueSI6ImJhc2UifSx7ImlkIjoiYmFzZS5hZG1pbiIsIm5hbWUiOiJhZG1pbiIsImNvbXBhbnkiOiJiYXNlIn1dLCJqd3QiOm51bGwsImNvbW11bml0eSI6ImJhc2UiLCJkYXRhIjp7fX0";
		
	SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
	InetSocketAddress address = new InetSocketAddress("bcprx.gbm.lan",8080);
	Proxy proxy = new Proxy(Proxy.Type.HTTP,address);	
        factory.setProxy(proxy);
        
    RestTemplate template = new RestTemplate();
    template.setRequestFactory(factory);
	System.out.println(template.getForObject("http://95.110.224.34:8080/community-0.0.2-SNAPSHOT/community/jwt?jwt={jwt}",Object.class,jwt));
}
}
