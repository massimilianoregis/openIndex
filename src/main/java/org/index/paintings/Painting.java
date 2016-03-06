package org.index.paintings;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.index.obj.Item;
import org.springframework.cache.annotation.Cacheable;

@Entity
@Table(name="painting")
public class Painting extends Item {
	
	/*
	@ElementCollection
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<String> taglie;
	*/
	@Cascade(value={CascadeType.ALL})
	@ManyToOne
	private Author author;
	private float height;
	private float width;	
	private String subject;
	private String technique;
	private String year;
	private String whereis;
	private String whereisurl;
	private String epoch;
	private String movement;
	private int printing;
	private String generalIndex;
		
	public Author getAuthor() {
		return author;
	}
	public void setAuthor(Author author) {
		this.author = author;
	}
	public float getHeight() {
		return height;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	public float getWidth() {
		return width;
	}
	public void setWidth(float width) {
		this.width = width;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getTechnique() {
		return technique;
	}
	public void setTechnique(String technique) {
		this.technique = technique;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public Where getWhere() {
		return new Where(this.whereis,this.whereisurl);
	}
	public void setWhere(Where whereis) {
		this.whereis = whereis.place;
		this.whereisurl=whereis.url;
	}
	public String getEpoch() {
		return epoch;
	}
	public void setEpoch(String epoch) {
		this.epoch = epoch;
	}

	public int getPrinting() {
		return printing;
	}
	public void setPrinting(int printing) {
		this.printing = printing;
	}
	public String getMovement() {
		return movement;
	}
	public void setMovement(String movement) {
		this.movement = movement;
	}
	public String getGeneralIndex() {
		return generalIndex;
	}
	public void setGeneralIndex(String generalIndex) {
		this.generalIndex = generalIndex;
	}
	public static class Where
		{
		private String place;
		private String url;
		public Where() {		
		}
		public Where(String place, String url) {
			this.place=place;
			this.url=url;
		}
		public String getUrl() {
			return url;
		}
		public String getPlace() {
			return place;
		}
		public void setPlace(String place) {
			this.place = place;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		}
	
}
