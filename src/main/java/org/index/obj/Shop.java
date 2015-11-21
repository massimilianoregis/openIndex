package org.index.obj;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.ElementCollection;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.index.Util;
import org.index.repository.Repositories;
import org.opencommunity.objs.User;
import org.opencommunity.security.AccessControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Shop {
	private @Id String id;
	private String name;
	private String background;
	private String logo;
	@OneToMany
	@ElementCollection
	@Cascade(value={CascadeType.ALL})
	@LazyCollection(LazyCollectionOption.FALSE)	
	private List<Media> gallery=new ArrayList<Media>();
	private String description;
	
	@Transient
	@Autowired
	private Util util;
	
	
	public Shop(){}
	public Shop(String id, String name){
		this.name=name;
		this.id=id;
	}
	public Shop(String name){
		this.name=name;
		this.id=UUID.randomUUID().toString();
	}
	
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getBackground() {
		return background;
	}
	public String getLogo() {
		return logo;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setBackground(String background) throws Exception{
		this.background = util.saveImage(background);
	}
	public void setLogo(String logo) throws Exception{
		this.logo = util.saveImage(logo);
	}
	
	@JsonIgnore
	public List<Pricing> getPricings(){
		return Repositories.pricing.findByShop(id);
	}
	@JsonIgnore
	public List<Catalogue> getCatalogues(){
		return Repositories.catalogue.findByShop(id);
	}
	@JsonIgnore
	public List<Category> getCategories(){
		return Repositories.category.findByShop(id);
	}
	@JsonIgnore
	public List<Item> getItems(){
		return Repositories.item.findByShop(id);
	}
		
	public List<Item> getItems(String catalogue){		
		//if(!Repositories.catalogue.findOne(catalogue).isRestrict())
		//	return Repositories.item.findByShopAndCataloguesId(this.name,catalogue);
		
		User user = AccessControl.getUser();
		if(user==null) return new ArrayList<Item>();
		List<String> list = (List)user.getData().get("catalogue");
		
		if(!list.contains(catalogue)) return new ArrayList<Item>();		
		return Repositories.item.findByShopAndCataloguesId(this.id,catalogue);
	}
	
	public void save(Pricing pr){
		Repositories.pricing.save(pr);
	}
	public void save(Category cat){
		Repositories.category.save(cat);
	}
	public void save(Item item){
		Repositories.item.save(item);
	}
	}
