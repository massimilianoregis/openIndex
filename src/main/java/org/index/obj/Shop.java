package org.index.obj;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.index.Util;
import org.index.repository.Repositories;
import org.opencommunity.objs.User;
import org.opencommunity.security.AccessControl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Shop {
	
	@Id 
	private String id;
	private String name;
	private String background;
	private String logo;
	private String tel;
	private String email;
	private String description;
	
	@OneToMany
	@ElementCollection
	@Cascade(value={CascadeType.ALL})
	@LazyCollection(LazyCollectionOption.FALSE)	
	private List<Media> gallery=new ArrayList<Media>();	
	
	@OneToMany
	@ElementCollection	
	@LazyCollection(LazyCollectionOption.FALSE)	
	private List<Pricing> pricings = new ArrayList<Pricing>();
	
	@OneToMany
	@ElementCollection	
	@LazyCollection(LazyCollectionOption.FALSE)	
	private List<Category> categories = new ArrayList<Category>();
	
	@Embedded
	private Address address;
	
	public Shop()
		{
		this.id=UUID.randomUUID().toString();
		}
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
	public void setBackground(String background) throws Exception	{
		this.background = Util.getInstance().saveImage(background);
	}
	public void setLogo(String logo) throws Exception{
		this.logo = Util.getInstance().saveImage(logo);
	}
	
	/*CURRENCY*/
	public void setCurrencies(String ... currencies){
		for(String item:currencies){
			addPricing("base", item);
			addPricing("web", item);
		}
	}

	public void addPricing(String name, String currency){
		if(id==null) save();
		this.pricings.add(new Pricing(id, name, currency));
	}
	//@JsonIgnore
	public List<Pricing> getPricings(){
		return this.pricings;
	}	
	//@JsonIgnore
	public List<Catalogue> getCatalogues(){
		return Repositories.catalogue.findByShop(id);
	}
	public void addCategories(String ... cats){		
		for(String item:cats)			
			categories.add(new Category(this,item));
	}
	//@JsonIgnore
	public List<Category> getCategories(){
		return categories;
	}
	@JsonIgnore
	public List<Item> getItems(){
		return Repositories.item.findByShop(id);
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public Item newItem(){
		return new Item(this);
	}
	
	
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
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
		
	public void save(){
		if(this.id==null) this.id=UUID.randomUUID().toString();
		Repositories.shop.save(this).getId();
	}
	}
