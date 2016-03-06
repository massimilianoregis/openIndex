package org.index.obj;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.index.service.IndexService.View;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
public class Page {
	@Id
	@JsonView(View.Shop.Full.class)
	private String id;
	@JsonView(View.Shop.Full.class)
	private String title;
	@JsonView(View.Shop.Full.class)
	private String description;
	@OneToMany
	@ElementCollection
	@Cascade(value={CascadeType.ALL})
	@LazyCollection(LazyCollectionOption.FALSE)
	@JsonView(View.Shop.Full.class)
	private List<Media> gallery=new ArrayList<Media>();	

	public String getId() {
		return id;
	}
	public String getTitle() {
		return title;
	}
	public String getDescription() {
		return description;
	}
	public List<Media> getGallery() {
		return gallery;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setGallery(List<Media> gallery) {
		this.gallery = gallery;
	}
}
