package org.opencommunity.objs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.ObjectMapper;


public class User implements Serializable 
{		
	
	private String mail;
	@JsonIgnore	
	private String uid;
	
	//@JsonIgnore
	private String psw;	
	private String firstName;
	private String lastName;
	private String root;
	private Date lastaccesstime;
	private Date sendRegisterMail;
	private String background;
	private String avatar;

	private Integer logtry=0;	
	private String registerId;
	
	private String jsondata;
	
	
	private Set<Role> roles = new HashSet<Role>();
	
	
	
	public User()
		{		
		}
	
	
	public void setMail(String mail) 				{this.mail = mail;}
	public void setPsw(String psw) 					{this.psw = psw;}
	public void setFirstName(String firstName) 		{this.firstName = firstName;}
	public void setLastName(String lastName) 		{this.lastName = lastName;}
	public void setRoot(String root) 				{this.root = root;}
	public void setRegisterId(String registerId) 	{this.registerId = registerId;}
	public void setRoles(Set<Role> roles) 			{this.roles = roles;}
	public void setName(String fname, String sname)	{this.firstName=fname; this.lastName=sname;}
	public void setUid(String uid) 					{this.uid = uid;}	
	
	
	
	
	public String getUid() 			{return uid;}
	public String getMail() 		{return mail;}	
	public String getFirstName() 	{return firstName;}
	public String getLastName() 	{return lastName;}
	public String getPsw() 			{return psw;}		
	public Set<Role> getRoles() 	{return roles;}
	public String getRegisterId() 	{return registerId;}
	public Integer getLogtry() 		{return logtry;}
	
	
	@JsonIgnore
	public File getRoot() 			{return new File(root);}
	

	
	public boolean canAccess(String role, String company)
		{
		for(Role r : roles)
			{			
			if(company==null && r.getName().equals(role)) 		return true;
			if(r.getId().equals(company+"."+role))				return true;
			}
		return false;
		}
	
	
	
	
	@JsonGetter
	public Map getData()
		{		
		try{
			return mapper.readValue(jsondata, Map.class);			
			}
		catch(Exception e){return new HashMap();}		
		}
	public void setData(String json)
		{
		this.jsondata=json;
		}
	@JsonSetter
	public void setData(Map map)
		{	
		try	{
			this.jsondata =mapper.writeValueAsString(map);
			}
		catch(Exception e){}		
		}
	public InputStream getFile(String file) throws IOException
		{
		System.out.println(this.root);
		return new FileInputStream(new File(this.root,file));
		}
	public Date getLastaccesstime() {
		return lastaccesstime;
	}
	public void setLastaccesstime(Date lastaccesstime) {
		this.lastaccesstime = lastaccesstime;
	}
	public Date getSendRegisterMail() {		
		return sendRegisterMail;
	}
	public void setSendRegisterMail(Date sendRegisterMail) {
		this.sendRegisterMail = sendRegisterMail;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) throws Exception{
		this.avatar = avatar;
	}
	public String getBackground() {
		return background;
	}
	public void setBackground(String background) throws Exception{
		this.background = background;
	}

	@Override
	public String toString() {
		return this.mail;
	}
	
	
	static private 	ObjectMapper mapper = new ObjectMapper();	

	public void extend(User user)
		{
		this.firstName=user.firstName;
		this.lastName=user.lastName;
		if(user.jsondata!=null)	this.jsondata=user.jsondata;
		if(user.psw!=null)		this.psw=user.psw;		
		}
}
