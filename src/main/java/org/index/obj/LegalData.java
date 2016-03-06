package org.index.obj;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.index.service.IndexService.View;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
public class LegalData {
	@Id
	@JsonView(View.Shop.Full.class)
	private String id;
	@JsonView(View.Shop.Full.class)
	private String name;
	@Embedded
	@JsonView(View.Shop.Full.class)
	private Address address;
	@JsonView(View.Shop.Full.class)
	private String piva;
	@JsonView(View.Shop.Full.class)
	private String rea;
	@JsonView(View.Shop.Full.class)
	private String capitale;
	
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address legalAddress) {
		this.address = legalAddress;
	}
	public String getPiva() {
		return piva;
	}
	public String getCapitale() {
		return capitale;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setPiva(String piva) {
		this.piva = piva;
	}
	public String getRea() {
		return rea;
	}
	public void setRea(String rea) {
		this.rea = rea;
	}
	public void setCapitale(String capitale) {
		this.capitale = capitale;
	}
	
}
