package org.opencommunity.client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class OpenCommunity {
	static private OpenCommunity instance;
	static public OpenCommunity getInstance(){return instance;}

	private String url;
	private String messages;
	public OpenCommunity(String url,String messages){
		OpenCommunity.instance=this;
		this.url=url;
		this.messages=messages;
	}
	
	public void addMessage(String from, String to,String message)	{
		Map map = new HashMap();
		map.put("from", from);
		map.put("to", to);
		map.put("message", message);
	
		new RestTemplate().getForObject(messages,String.class,map);
	}
	public void send(String title, String message,Map payload, String ... users){
		send(title,message,payload,Arrays.asList(users));
	}
	public void send(String title, String message,Map payload, List<String> users){		
		Map map = new HashMap();
			map.put("title", title);
			map.put("text", message);
			map.put("payload", payload);
			map.put("mail", users);			
				
		new RestTemplate().postForObject(url,map,String.class);
	}
	
	public static void main(String[] args) throws Exception{
		/*
		String url="http://95.110.228.140:8080/openCommunity/community/notify/{mail}/send?msg={msg}&title={title}";
//		new OpenCommunity(url,"").send("pippo", "pippo","", "massimiliano.regis@ekaros.it");
		String messages="http://95.110.228.140:8080/openCommunity/message/add/?from={from}&message={message}&to={to}";
		new OpenCommunity(url,messages).addMessage("michipaperino@gmail.com","5505e0d5-b23e-4b10-8550-f6445b75ca75","ciao");;*/
		
		Map payload = new HashMap();
			payload.put("type", "shop");
		Map map = new HashMap();
		map.put("title", "title");
		map.put("text", "message");		
		map.put("payload", payload);
		map.put("mail", new String[]{"massimiliano.regis@gmail.com"});
		
			
		
		String url ="http://95.110.228.140:8080/openCommunity/community/notify/send";		
		System.out.println(new RestTemplate().postForObject(url,map,String.class));
	}
}
