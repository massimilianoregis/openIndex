package org.index.obj;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Shipping {
	@Id
	private String id;
	private String country;
	private String currency;
	private Double price;
	
	public Shipping(){
		this.id=UUID.randomUUID().toString();
	}
	public String getId() {
		return id;
	}
	public String getCountry() {
		return country;
	}
	public String getCurrency() {
		return currency;
	} 
	public Double getPrice() {
		return price;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	} 
	public void setPrice(Double price) {
		this.price = price;
	}
}
