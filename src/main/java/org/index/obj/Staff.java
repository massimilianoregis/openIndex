package org.index.obj;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Staff {
	@Id
	private String mail;
	private String role; 
	
	public Staff() {
		// TODO Auto-generated constructor stub
	}
	public Staff(String mail, String role){
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
