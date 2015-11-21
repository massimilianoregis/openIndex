package org.index.obj;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.index.repository.Repositories;
import org.opencommunity.security.AccessControl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class Item 
	{
	@Id
	private String id;
	private String name;
	private String code;
	private String url;
	private boolean visible=true;	
	private String shop;
	@Column(columnDefinition="TEXT")
	private String description;
	
	private Date creationDate;
	private String state;
	
	@OneToMany
	@ElementCollection
	@Cascade(value={CascadeType.ALL})
	@LazyCollection(LazyCollectionOption.FALSE)	
	private List<Media> gallery=new ArrayList<Media>();
	
	@ElementCollection
	@CollectionTable(name="prices")	
	@MapKeyColumn(name="pricing")	
	@Column(name="value")
	@Cascade(value={CascadeType.ALL})
	@LazyCollection(LazyCollectionOption.FALSE)
	private Map<String,Float> prices=new HashMap<String, Float>();
		
	@JsonIgnore
	@ManyToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<Category> categories = new HashSet<Category>();
	
	@JsonIgnore
	@ManyToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<Catalogue> catalogues = new HashSet<Catalogue>();
	
	@JsonIgnore
	@ManyToMany
	@LazyCollection(LazyCollectionOption.TRUE)
	private Set<Item> suggestItem = new HashSet<Item>();
	
	
	@Column(columnDefinition="TEXT")
	@JsonIgnore
	private String extra;
	
	private Integer quantity;
	
	
	public Item()
		{
		this.id=UUID.randomUUID().toString();
		}
	public Item(String code)
		{
		this.id=UUID.randomUUID().toString();
		this.code=code;		
		}
	public Item(String code, String name)
		{
		this.id=UUID.randomUUID().toString();
		this.code=code;
		this.name=name;
		this.creationDate=new Date();
		}
	
	public boolean isVisible()
		{	
		return visible;
		}
	public boolean getVisible()
		{
		return visible;
		}
	public void setVisible(boolean visible)
		{
		this.visible=visible;
		}
	public void setPrice(String pricing, Float value)
		{
		prices.put(pricing,value);
		}
	
	public void addCategory(String name)
		{
		Category cat = Repositories.category.findOne(name);		
		this.categories.add(cat);
		}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
		if(id==null) this.id=UUID.randomUUID().toString();		
	}
	public String getShop() {
		return shop;
	}
	public void setShop(String shop) {
		this.shop = shop;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	
	public Float getPrice()
		{
		String price ="web";
		try{price=(String)AccessControl.getUser().getData().get("pricing");}catch(Exception e){} 
				
		Float value = prices.get(getShop()+"."+price);
		if(value==null)	value = prices.get(price);
		return value;
		}
	public Float getBasePrice()
		{
		Float value = prices.get(getShop()+".base");
		if(value==null)	value = prices.get("base");
		return value;
		}
	
	public Map<String,Float> getPrices() {
		if(!AccessControl.canAccess("admin",null)) return null;
		return prices;
	}
	public void setPrices(Map<String,Float> prices) {
		this.prices = prices;		
	}
	
	
	@JsonGetter	
	public List<String> getCategories() {
		List<String> result = new ArrayList<String>();
		for(Category ct :this.categories)
			result.add(ct.getId());
		return result;
	}
	
	@JsonSetter
	public void setCategories(List<String> categories) 
		{		
		for(String catName :categories)
			{
			Category cat = Repositories.category.findOne(catName);
			this.categories.add(cat);
			}
		}
	@JsonGetter	
	public List<String> getCatalogues() {
		List<String> result = new ArrayList<String>();
		for(Catalogue ct :this.catalogues)			
			result.add(ct.getName());
		return result;
	}
	
	@JsonSetter
	public void setCatalogues(List<String> categories) 
		{		
		for(String catName :categories)
			{
			Catalogue cat = Repositories.catalogue.findOne(catName);			
			this.catalogues.add(cat);
			}
		}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public List<Media> getGallery() {
		return gallery;
	}
	public void setGallery(List<Media> gallery) {
		this.gallery = gallery;
	}
	
	public Set<Item> getSuggestItem() {
		return suggestItem;
	}
	public void setSuggestItem(Set<Item> suggestItem) {
		this.suggestItem = suggestItem;
	}
	public void addSuggestItem(Item item)
	{
		this.suggestItem.add(item);
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	static private 	ObjectMapper mapper = new ObjectMapper();	
	@JsonGetter
	public Map getExtra()
		{		
		try{
			return mapper.readValue(extra, Map.class);			
			}
		catch(Exception e){return new HashMap();}		
		}
	
	@JsonSetter
	public void setExtra(Map map)
		{	
		try	{
			this.extra =mapper.writeValueAsString(map);
			}
		catch(Exception e){}		
		}
	
	}
