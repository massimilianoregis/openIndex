package org.index.obj;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;

import org.index.repository.Repositories;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
@Entity
public class Catalogue 
	{
	@Id
	private String id;
	private String name;	
	private String shop;
	
	private boolean restrict=false; 
	
	public Catalogue(){}
	public Catalogue(String name)
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
	public boolean isRestrict() {
		return restrict;
	}
	public void setRestrict(boolean restrict) {
		this.restrict = restrict;
	}
	public void remove()
		{
		Repositories.catalogue.delete(this.name);
		}
	
	@PrePersist
	public void prePersist()
		{
		this.id=this.shop+"."+this.name;
		}
	
	
	}
