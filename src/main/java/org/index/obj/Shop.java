package org.index.obj;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.index.Util;
import org.index.news.News;
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
	private String description;
	
	private String tel;	
	private String email;
	private String website;
	private String facebook;
	private String twitter;
	private String linkedin;
	
	private String code;
	
	@Embedded
	private Address address;
	
	@OneToOne
	@Cascade(value={CascadeType.ALL})
	private LegalData legalData;
	
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
	@Cascade(value={CascadeType.ALL})
	@LazyCollection(LazyCollectionOption.FALSE)	
	private List<Shipping> shipping=new ArrayList<Shipping>();	
	
	@OneToMany
	@ElementCollection	
	@LazyCollection(LazyCollectionOption.FALSE)	
	private List<Category> categories = new ArrayList<Category>();
	
	@OneToMany
	@ElementCollection	
	@Cascade(value={CascadeType.ALL})
	@LazyCollection(LazyCollectionOption.FALSE)	
	private List<News> news = new ArrayList<News>();
	
	@OneToOne	
	@Cascade(value={CascadeType.ALL})		
	private Page whoAreWe;
	
	
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
	public String getDescription() {
		return description;
	}	
	public LegalData getLegalData() {
		return legalData;
	}
	//@JsonIgnore
	public List<Pricing> getPricings(){
		return this.pricings;
	}	
	//@JsonIgnore
	public List<Catalogue> getCatalogues(){
		return Repositories.catalogue.findByShop(id);
	}
	//@JsonIgnore
	public List<Category> getCategories(){
		return categories;
	}
	@JsonIgnore
	public List<Item> getItems(){
		return Repositories.item.findByShop(id);
	}
	public Address getAddress() {
		return address;
	}
	@JsonIgnore
	public List<News> getLatestNews(){
		return news;
	}	
	public Page getWhoarewe() {
		return whoAreWe;
	}
	public String getTel() {
		return tel;
	}
	public String getEmail() {
		return email;
	}
	public String getFacebook() {
		return facebook;
	}
	public List<Media> getGallery() {
		return gallery;
	}
	public String getLinkedin() {
		return linkedin;
	}
	public String getTwitter() {
		return twitter;
	}
	public String getWebsite() {
		return website;
	}
	public String[] getCurrencies(){
		Set<String> set = new HashSet<String>();
		for(Pricing pr:this.pricings)
			set.add(pr.getCurrency());
		return set.toArray(new String[0]);
		}	
	public List<Shipping> getShipping() {
		return shipping;
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
	public void setLegalData(LegalData legalData) {
		if(legalData==null) return;
		this.legalData = legalData;		
		legalData.setId(getId());
	}
	/*CURRENCY*/
	public void setCurrencies(String ... currencies){
		List<Pricing> pr = new ArrayList<Pricing>();
		for(String item:currencies){
			pr.add(new Pricing(getId(),"base", item).save());
			pr.add(new Pricing(getId(),"web", item).save());
		}
		this.pricings=pr;
	}

	public void addPricing(String name, String currency){
		if(id==null) save();
		this.pricings.add(new Pricing(id, name, currency));		
	}
	
	public void addCategories(String ... cats){		
		List<Category> pr = new ArrayList<Category>();
		for(String item:cats){
			pr.add(new Category(item,getId()).save());			
		}
		this.categories=pr;
	}
	
	
	public void setTel(String tel) {
		this.tel = tel;
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
	public void setWebsite(String website) {
		this.website = website;
	}
	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}
	public void setLinkedin(String linkedin) {
		this.linkedin = linkedin;
	}
	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}
	
	public void setShipping(List<Shipping> shipping) {
		this.shipping = shipping;
		
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	
	/*NEWS*/
	public void addNews(News news){
		this.news.add(news);
	}
	
	public void setLatestNews(List<News> news){
		this.news=news;
	}
	public void setWhoarewe(Page whoAreWe) {
		if(whoAreWe==null) return;
		whoAreWe.setId(this.id);
		whoAreWe.setTitle("Who are we");
		this.whoAreWe = whoAreWe;		
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
		Repositories.shop.save(this);
	}
	}
