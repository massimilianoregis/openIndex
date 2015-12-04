package org.index.obj;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class LegalData {
	@Id
	private String id;
	private String name;
	@Embedded
	private Address address;
	private String piva;
	private String rea;
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
