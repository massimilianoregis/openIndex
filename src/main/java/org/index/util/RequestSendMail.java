package org.index.util;

import java.util.Map;

public class RequestSendMail
		{
		String from;
		String to;
		String subject;
		String template;			
		Map data;
		
		public RequestSendMail(String from,String to, String subject, String template, Map data){
			this.from		=	from;
			this.to			=	to;
			this.subject	=	subject;
			this.template	=	template;
			this.data		=	data;
			}
		public String getFrom() 					{return from;}
		public void setFrom(String from) 			{this.from = from;}
		public String getTo() 						{return to;}
		public void setTo(String to) 				{this.to = to;}
		public String getSubject() 					{return subject;}
		public void setSubject(String subject) 		{this.subject = subject;}
		public String getTemplate() 				{return template;}
		public void setTemplate(String template) 	{this.template = template;}
		public Map getData() 						{return data;}
		public void setData(Map data) 				{this.data = data;}		
		}