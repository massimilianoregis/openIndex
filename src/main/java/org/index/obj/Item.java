package org.index.obj;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Access;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonPropertyOrder({"shop"})
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
	private Float qta;
	private Boolean haveColors;
	
	
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
		
	@JsonIgnore
	@ManyToMany(fetch=FetchType.LAZY)	
	@Cascade(value={CascadeType.SAVE_UPDATE})
	private Set<Category> categories = new HashSet<Category>();
	
	@JsonIgnore
	@ManyToMany(fetch=FetchType.LAZY)	
	//@LazyCollection(LazyCollectionOption.TRUE)
	@Cascade(value={CascadeType.SAVE_UPDATE})
	private List<Catalogue> catalogues = new ArrayList<Catalogue>();	
	
	@JsonIgnore
	@ManyToMany(fetch=FetchType.LAZY)	
	@Cascade(value={CascadeType.ALL})
	private List<Item> suggestItem = new ArrayList<Item>();
	
	@JsonIgnore
	@ElementCollection
	@Cascade(value={CascadeType.ALL})
	@OneToMany(fetch=FetchType.LAZY)		
	private List<Color> colors = new ArrayList<Color>();
	
	
	@Column(columnDefinition="TEXT")
	@JsonIgnore
	private String extra;
	
	
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
		if(cat==null)
			cat = Repositories.category.findByShopAndName(this.shop, name);
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
		try	{return (String)AccessControl.getUser().getData().get("currency");}
		catch(Exception e){return "EUR";}
	}
	public Float getQta() {
		return qta;
	}
	public void setQta(Float qta) {
		this.qta = qta;
	}
	
	static private String[] currencyPriority ={"EUR","USD"};
	@Transient
	private String currency;
	public String getCurrency()
		{		
		if(this.currency!=null) return currency;
		
		String currency =getUserCurrency();
		if(currency!=null)
			for(Price item :this.prices)
				if(currency.equals(item.getCurrency()))
					{
					this.currency=currency;
					return currency;
					}
		
		for(String cr :Item.currencyPriority)
			for(Price item :this.prices)
				if(cr.equals(item.getCurrency()))
					{
					this.currency=cr;
					return cr;
					}

		return currency;
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
			if(price==null) price="web";
			return getPrice(getCurrency(),price).getValue();
			}
		catch(NullPointerException e)
			{
			return null;
			}
		}
	public Double getBasePrice()
		{
		try	{
			return getPrice(getCurrency(),"base").getValue();
			}
		catch(NullPointerException e)
			{
			return null;
			}
		}
	
	
	public List<Price> getPrices() {						
		try{if(!(AccessControl.isAdmin(this.getShop()) ||
			 AccessControl.canAccess("admin"))) return null;
			}
		catch(Exception e){return null;}
		return prices;
	}
	public void setPrices(Price ... prices) {		
//		for(Price price:prices)
//			price.setItem(this);
		this.prices = Arrays.asList(prices);		
	}
	
	
	@JsonGetter	
	public List<String> getCategories() {
		List<String> result = new ArrayList<String>();
		for(Category ct :this.categories)
			result.add(ct.getName());
		return result;
	}
	
	public void addImage(String image) throws Exception{
		this.gallery.add(new Media(image));
	}
	
	@JsonSetter
	public void setCategories(String ... categories) 
		{		
		for(String catName :categories)			
			addCategory(catName);			
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
	
	@JsonGetter
	public List<Color> getColors()
		{
		if(haveColors==null || !haveColors) return null;
		return colors;
		}
	@JsonSetter
	public void setColors(List<Color> colors)
		{
		haveColors= colors!=null && colors.size()>0;
		
		this.colors=colors;
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
	
	}
	
	@Entity
	@JsonIgnoreProperties(ignoreUnknown=true)	
	public static class Price
		{
		@Id
		private String id;
		private Double value;
		@ManyToOne		
		@JoinColumn
		private Pricing pricing;
		private String currency;
		
		
		
		public Price() {		
		}
		
		@JsonCreator	
		public Price(
				@JsonProperty("shop") 		String shop, 
				@JsonProperty("name") 		String name, 
				@JsonProperty("currency") 	String currency,
				@JsonProperty("value") 	Double value) throws Exception{			
			this.id=UUID.randomUUID().toString();
			this.pricing = Repositories.pricing.findByShopAndNameAndCurrency(shop,name,currency);
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
			if(pricing==null) return null;
			return pricing.getCurrency();
		}
		public String getName(){
			if(pricing==null) return null;
			return pricing.getName();
		}
		public Double getValue() {
			return value;
		}	
		
			
		public void setValue(Double value) {
			this.value = value;
		}
		
		/*
		@JsonUnwrapped
		public void setData(Data data)
			{
			this.pricing = Repositories.pricing.findByShopAndNameAndCurrency(data.getShop(), data.getName(), data.getCurrency());
			}*/
		
		@PreUpdate
		@PrePersist
		public void prePersist(){			
			if(id==null) id=UUID.randomUUID().toString();
			
			
		}
		@Override
		public String toString() {
		
			return id+" "+pricing+" "+value+"--";
		}
		
		}
	
	public static class Data{
		private String currency;
		private String name;
		private String shop;
		public String getCurrency() {
			return currency;
		}
		public void setCurrency(String currency) {
			this.currency = currency;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getShop() {
			return shop;
		}
		public void setShop(String shop) {
			this.shop = shop;
		}
		
	} 
	
	}
