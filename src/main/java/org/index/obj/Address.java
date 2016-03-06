package org.index.obj;

import javax.persistence.Embeddable;

import org.index.service.IndexService.View;

import com.fasterxml.jackson.annotation.JsonView;


@Embeddable
public class Address {
	
	@JsonView(View.Shop.Full.class)
	private String line1;
	@JsonView(View.Shop.Full.class)
	private String line2;
	@JsonView(View.Shop.Full.class)
	private String country;
	@JsonView(View.Shop.Full.class)
	private String city;
	@JsonView(View.Shop.Full.class)
	private String zip;
	public String getLine1() {
		return line1;
	}
	public void setLine1(String line1) {
		this.line1 = line1;
	}
	public String getLine2() {
		return line2;
	}
	public void setLine2(String line2) {
		this.line2 = line2;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	
	
}
