package org.index.obj;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;

import org.index.repository.Repositories;


@Entity
public class Category 
	{
	@Id
	private String id;
	private String name;	
	private String shop;
	
	public Category(){}
	public Category(String name)
		{		
		this.name=name;		
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
	
	@PrePersist
	public void prePersist()
		{
		this.id=this.shop+"."+this.name;
		}
	}
