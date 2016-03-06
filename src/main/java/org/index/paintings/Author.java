package org.index.paintings;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.index.obj.Media;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class Author {
	@Id
	private String uid;
	private String firstName;
	private String lastName;
	
	@JsonIgnore private Date borndate;
	@JsonIgnore private String borncity;
	@JsonIgnore private String borncountry;
	@JsonIgnore private Date diedate;
	@JsonIgnore private String diecity;
	@JsonIgnore private String diecountry;
	
	@OneToMany
	@Cascade(value={CascadeType.ALL})
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Media> gallery=new ArrayList<Media>();
	
	@Column(columnDefinition="TEXT")
	private String biography;
	
	public Author() {
		this.uid=UUID.randomUUID().toString();
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Time getBorn()
		{
		return new Time(this.borndate,this.borncity,this.borncountry);
		}
	public Time getDie()
		{
		return new Time(this.diedate,this.diecity,this.diecountry);
		}
	public void setDie(Time time)
		{
		this.diecity=time.getCity();
		this.diecountry=time.getCountry();
		this.diedate=time.getDate();
		}
	public void setBorn(Time time)
		{
		this.borncity=time.getCity();
		this.borncountry=time.getCountry();
		this.borndate=time.getDate();
		}
	
	public String getBiography() {
		return biography;
	};
	public void setBiography(String biography) {
		this.biography = biography;
	}
	
	public List<Media> getGallery() {
		return gallery;
	}
	public void setGallery(List<Media> gallery) {
		this.gallery = gallery;
	}
	public static class Time
		{
		private Date date;
		private String city;
		private String country;
		public Time() {
		}
		public Time(Date date, String city, String country)
			{
			this.date=date;
			this.city=city;
			this.country=country;
			}
		public Date getDate() {
			return date;
		}
		public void setDate(Date date) {
			this.date = date;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getCountry() {
			return country;
		}
		public void setCountry(String country) {
			this.country = country;
		}
		
		}
}
