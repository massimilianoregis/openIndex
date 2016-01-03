package org.index;

import org.opencommunity.client.OpenCommunity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IndexConfiguration 
	{
	@Value("${community.send}") private String send;
	@Value("${community.messages}") private String messages;
	@Bean
	public OpenCommunity community()
		{
		return new OpenCommunity(send,messages);
		}
		
	
	}
