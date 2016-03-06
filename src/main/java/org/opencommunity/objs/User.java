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
	private String background;
	private String avatar;			
	private Map data;
	
	private Set<Role> roles = new HashSet<Role>();
	
	public User()		{}
	
	public void setBackground(String background)	{this.background = background;}
	public void setAvatar(String avatar) 			{this.avatar = avatar;}
	public void setMail(String mail) 				{this.mail = mail;}
	public void setPsw(String psw) 					{this.psw = psw;}
	public void setFirstName(String firstName) 		{this.firstName = firstName;}
	public void setLastName(String lastName) 		{this.lastName = lastName;}
	public void setRoot(String root) 				{this.root = root;}
	public void setRoles(Set<Role> roles) 			{this.roles = roles;}
	public void setName(String fname, String sname)	{this.firstName=fname; this.lastName=sname;}
	public void setUid(String uid) 					{this.uid = uid;}
	public void setData(Map map)					{this.data=map;}
	
	public String getBackground() 	{return background;}
	public String getAvatar() 		{return avatar;}
	public String getUid() 			{return uid;}
	public String getMail() 		{return mail;}	
	public String getFirstName() 	{return firstName;}
	public String getLastName() 	{return lastName;}
	public String getPsw() 			{return psw;}		
	public Set<Role> getRoles() 	{return roles;}	
	public Map getData()			{return data;}
	
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

	public InputStream getFile(String file) throws IOException
		{
		System.out.println(this.root);
		return new FileInputStream(new File(this.root,file));
		}
	
	@Override
	public String toString() {
		return this.mail;
	}

}
