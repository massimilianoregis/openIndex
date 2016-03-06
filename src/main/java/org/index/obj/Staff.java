package org.index.obj;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.index.service.IndexService.View;

import com.fasterxml.jackson.annotation.JsonView;

@Entity(name="shopstaff")
public class Staff {
	@Id
	private String id;
	@JsonView(View.Shop.Full.class)
	private String mail;
	@JsonView(View.Shop.Full.class)
	private String role; 
	
	public Staff() {
		this.id=UUID.randomUUID().toString();
	}
	public Staff(String mail, String role){
		this.id=UUID.randomUUID().toString();
		this.mail=mail;
		this.role=role;		
	}
	
	public String getMail() {
		return mail;
	}
	public String getRole() {
		return role;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
	
	@Override
	public String toString() {
	
		return getMail();
	}
}
