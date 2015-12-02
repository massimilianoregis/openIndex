package org.index.obj;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.index.repository.Repositories;
import org.opencommunity.security.AccessControl;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"shop"})
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

	@OneToMany
	@ElementCollection
	@Cascade(value={CascadeType.ALL})
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Price> prices = new ArrayList<Price>();
	
//	@ElementCollection
//	@CollectionTable(name="prices")	
//	@MapKeyColumn(name="pricing")	
//	@Column(name="value")
//	@Cascade(value={CascadeType.ALL})
//	@LazyCollection(LazyCollectionOption.FALSE)
//	private Map<String,Float> prices=new HashMap<String, Float>();
			
		
	@JsonIgnore
	@ManyToMany
	@ElementCollection
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Category> categories = new ArrayList<Category>();
	
	@JsonIgnore
	@ManyToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Catalogue> catalogues = new ArrayList<Catalogue>();
	
	@JsonIgnore
	@ManyToMany
	@LazyCollection(LazyCollectionOption.TRUE)
	private List<Item> suggestItem = new ArrayList<Item>();
	
	
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
	public Item(Shop shop){
		this.id=UUID.randomUUID().toString();
		if(shop.getId()==null) shop.save();
		this.shop=shop.getId();
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
	public void setPrice(String name, String currency, Double value) throws Exception
		{				
		prices.add(new Price(shop,name,currency,value));
		}
	public void setPrice(String pricing, Double value) throws Exception
		{				
		prices.add(new Price(pricing, value));
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
	
	private String getUserCurrency(){
		return "EUR";
	}
	
	private Price getPrice(String currency, String name) {
		
		for(Price item :this.prices)
			if(item.getCurrency().equals(currency) && item.getName().equals(name))
				return item;
		return null;
		}
	public Double getPrice()
		{
		String price ="web";		
		try{price=(String)AccessControl.getUser().getData().get("pricing");}catch(Exception e){} 
		try{
			return getPrice(getUserCurrency(),price).getValue();
			}
		catch(NullPointerException e)
			{
			return null;
			}
		}
	public Double getBasePrice()
		{
		try	{
			return getPrice(getUserCurrency(),"base").getValue();
			}
		catch(NullPointerException e)
			{
			return null;
			}
		}
	
	
	public List<Price> getPrices() {		
		//if(!AccessControl.canAccess("admin",null)) return null;
		return prices;
	}
	public void setPrices(Price ... prices) {		
		this.prices = Arrays.asList(prices);		
	}
	
	
	@JsonGetter	
	public List<String> getCategories() {
		List<String> result = new ArrayList<String>();
		for(Category ct :this.categories)
			result.add(ct.getId());
		return result;
	}
	
	@JsonSetter
	public void setCategories(String ... categories) 
		{		
		for(String catName :categories)
			{
			Category cat = Repositories.category.findOne(catName);			
			if(cat==null) cat = Repositories.category.findByShopAndName(getShop(), catName);
			
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
	
	public List<Item> getSuggestItem() {
		return suggestItem;
	}
	public void setSuggestItem(List<Item> suggestItem) {
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
	public void save(){
		Repositories.item.save(this);
		System.err.println(this.categories);
	}
	
	@Entity
	public static class Price
		{
		@Id
		private String id;
		private Double value;
		@ManyToOne		
		@JoinColumn
		private Pricing pricing;

		
		@Transient private String currency;
		@Transient private String name;
		@Transient private String shop;
		
		public Price() {		
		}
		public Price(String shop, String name, String currency,Double value) throws Exception{
			id=UUID.randomUUID().toString();
			pricing = Repositories.pricing.findByShopAndNameAndCurrency(shop,name,currency);
			
			this.value=value;			
		}
		public Price(String pricing, Double value){
			id=UUID.randomUUID().toString();
			this.pricing = Repositories.pricing.findOne(pricing);
			this.value=value;
		}
		public Price(Pricing pricing, Double value){
			id=UUID.randomUUID().toString();
			this.pricing=pricing;
			this.value=value;
		}
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		@JsonIgnore
		public Pricing getPricing() {
			return pricing;
		}
		public String getCurrency() {
			return pricing.getCurrency();
		}
		public String getName(){
			return pricing.getName();
		}
		public void setCurrency(String currency){
			this.currency=currency;
		}
		public void setName(String name){
			this.name=name;
		}
		public Double getValue() {
			return value;
		}
		public void setValue(Double value) {
			this.value = value;
		}
		
		
		@PreUpdate
		@PrePersist
		public void prePersist(){
			if(id==null) id=UUID.randomUUID().toString();
			if(this.pricing==null) this.pricing = Repositories.pricing.findByShopAndNameAndCurrency(this.shop, this.name, this.currency);
		}
		@Override
		public String toString() {
		
			return id+" "+pricing+" "+value+"--";
		}
		
		}
	}
