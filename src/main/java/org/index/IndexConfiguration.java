package org.index;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.opencommunity.client.OpenCommunity;
import org.opencommunity.security.AccessControl;
import org.opencommunity.util.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class IndexConfiguration
	{
	@Value("${community.send}") 	private String send;
	@Value("${community.messages}") private String messages;
	@Value("${community.mail}") 	private String mail;
	
		
	@Bean
	public OpenCommunity community()
		{
		return new OpenCommunity(send,messages,mail);
		}
		
	@Bean
	public CacheManager cacheManager() {			    
	    return new ConcurrentMapCacheManager("shops","item");
	}
	
	@Bean
	public Client client()
		{
		return new Client(null);
		}
	
	@Bean 
	public AccessControl accessControl(HttpServletRequest request)
		{
		return new AccessControl(request, client());
		}
	
	@Bean Index index()
		{
		Index idx =new Index();
			
		return idx;
		}
	}
