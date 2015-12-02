package org.index.obj;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;

import org.index.repository.Repositories;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Category 
	{
	@Id
	private String id;
	private String name;	
	private String shop;
	
	public Category(){}
	public Category(Shop shop,String name)
		{
		if(shop.getId()==null) shop.save();
		this.shop=shop.getId();
		this.name=name;
		}
	public Category(String name,String shop)
		{		
		
		this.name=name;		
		this.shop=shop;
		}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() 			{return name;}
	public void setName(String name) 	{this.name = name;}
	public String getShop() {
		return shop;
	}
	public void setShop(String shop) {
		this.shop = shop;
	}
	public void remove()
		{
		Repositories.category.delete(this.name);
		}
	@Override
	public String toString() {		
		return "category:"+id+" "+name;
		}
	@PrePersist
	public void prePersist()
		{
		if(this.id==null) this.id=this.shop+"."+this.name;
		}
	}
