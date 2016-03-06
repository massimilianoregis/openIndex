package org.index.obj;

import java.util.Currency;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;

import org.index.repository.Repositories;
import org.index.service.IndexService.View;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@JsonInclude(Include.NON_NULL)
public class Pricing 
	{
	@Id
	@JsonView(View.Shop.Full.class)
	private String id;
	@JsonView(View.Shop.Full.class)	
	private String name;	
	@JsonView(View.Shop.Full.class)
	private String description;	
	private String shop;
		
	private String code;
	private String pass;	
	
	@JsonView(View.Shop.Full.class)
	private String currency;

	public Pricing(){
		this.id=UUID.randomUUID().toString();
	}
	
	public Pricing(String shop, String name, String currency)
		{		
		this.id=UUID.randomUUID().toString();
		this.name=name;
		this.shop=shop;
		this.currency=currency;
		}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	
	public String getShop() {
		return shop;
	}
	public void setShop(String shop) {
		this.shop = shop;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public Pricing save(){
		return Repositories.pricing.save(this);
	}
	
	}
