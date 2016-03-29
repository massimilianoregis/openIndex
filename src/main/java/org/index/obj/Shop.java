package org.index.obj;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.index.Util;
import org.index.categories.GoodClass;
import org.index.news.News;
import org.index.repository.Repositories;
import org.index.service.IndexService.View;
import org.index.util.RequestSendMail;
import org.opencommunity.client.OpenCommunity;
import org.opencommunity.objs.User;
import org.opencommunity.security.AccessControl;
import org.opencommunity.util.AutomaticPassword;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class Shop {
	
	@Id
	@JsonView(View.Shop.Base.class)	private String id;	
	@JsonView(View.Shop.Base.class)	private String name;
	@JsonView(View.Shop.Base.class)	private String background;
	@JsonView(View.Shop.Base.class)	private String logo;
	@JsonView(View.Shop.Base.class)	private String description;
	@JsonView(View.Shop.Full.class)	private String style;
	
	@JsonView(View.Shop.Full.class)	private String tel;
	@JsonView(View.Shop.Full.class)	private String email;
	@JsonView(View.Shop.Full.class)	private String website;
	@JsonView(View.Shop.Full.class)	private String facebook;
	@JsonView(View.Shop.Full.class)	private String twitter;
	@JsonView(View.Shop.Full.class)	private String linkedin;
	@JsonView(View.Shop.Base.class)	private Boolean visible;
	
	//friendlycode
	@JsonView(View.Shop.Base.class)	private String code;
	
	@Embedded
	@JsonView(View.Shop.Full.class)	private Address address;
	
	@Embedded
	@JsonView(View.Shop.Full.class)	private Paypal paypal;
	
	@Embedded
	@JsonView(View.Shop.Full.class)	private Gps gps;
	
	//private List<String> goodType;
	
	@OneToOne
	@Cascade(value={CascadeType.ALL})
	@JsonView(View.Shop.Full.class)	
	private LegalData legalData;
	
	@OneToMany
	@ElementCollection
	@Cascade(value={CascadeType.ALL})
	@LazyCollection(LazyCollectionOption.FALSE)
	@JsonView(View.Shop.Full.class)	private List<Media> gallery=new ArrayList<Media>();	
	
	@OneToMany
	@ElementCollection
	@Cascade(value={CascadeType.ALL})
	@LazyCollection(LazyCollectionOption.FALSE)
	@JsonView(View.Shop.Full.class)
	private List<Pricing> pricings = new ArrayList<Pricing>();
	
	/*
	@OneToMany
	@ElementCollection
	@Cascade(value={CascadeType.ALL})
	@LazyCollection(LazyCollectionOption.FALSE)
	@JsonView(View.Shop.Full.class)
	private List<Currency> currencies = new ArrayList<Currency>();
	*/
	
	@OneToMany
	@ElementCollection
	@Cascade(value={CascadeType.ALL})
	@LazyCollection(LazyCollectionOption.FALSE)	
	@JsonView(View.Shop.Full.class)
	private List<Shipping> shipping=new ArrayList<Shipping>();	
	
	@OneToMany
	@ElementCollection
	@Cascade(value={CascadeType.ALL})
	@LazyCollection(LazyCollectionOption.FALSE)	
	@JsonView(View.Shop.Full.class)
	private List<Category> categories = new ArrayList<Category>();
	
	@OneToMany
	@ElementCollection	
	@Cascade(value={CascadeType.ALL})
	@LazyCollection(LazyCollectionOption.FALSE)	
	private List<News> news = new ArrayList<News>();
		
	@ManyToMany(fetch=FetchType.LAZY)	
	@Cascade(value={CascadeType.ALL})
	@JsonView(View.Shop.Full.class)
	private List<GoodClass> goodClasses = new ArrayList<GoodClass>();
	
	@OneToOne	
	@Cascade(value={CascadeType.ALL})
	@JsonView(View.Shop.Full.class)
	private Page whoAreWe;
	
	@ManyToMany(fetch=FetchType.LAZY)
	@Cascade(value={CascadeType.ALL})
	@JsonView(View.Shop.Full.class)
	private List<Staff> staff = new ArrayList<Staff>();
		
	@ElementCollection(fetch=FetchType.LAZY)
	@JsonView(View.Shop.Full.class) private List<String> goodTypes = new ArrayList<String>();
	
	
	public Shop()
		{		
		init();
		}
	public Shop(String id, String name){
		this.name=name;
		this.id=id;
		init();
	}
	public Shop(String name){
		this.name=name;
		init();
	}
	
	private void init(){
		if(this.code==null) this.code= new AutomaticPassword().newPassword(8).toUpperCase();
		if(this.id==null) this.id=UUID.randomUUID().toString();
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
	@JsonGetter
	@JsonView(View.Shop.Full.class)
	public String[] getCurrencies(){
		Set<String> set = new HashSet<String>();
		for(Pricing pr:this.pricings)
			set.add(pr.getCurrency());
		return set.toArray(new String[0]);
		}	
	public List<Shipping> getShipping() {
		return shipping;
	}
	
	public String getCode() {
		return code;
	}
	public String getStyle() {
		return style;
	}
	
	/*gps*/
	public Gps getGps() {
		return gps;
	}
	public void setGps(Gps gps) {
		this.gps = gps;
	}
	
	
	/*good classes*/
	public List<GoodClass> getGoodClasses() {
		return goodClasses;
	}
	public void setCategories(List<Category> categories) {
		this.categories = categories;
		for(Category c:categories )
			c.setShop(this.id);
	}
	
	
	public void setCode(String code) {
		this.code = code;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setId(String id) {
		this.id = id;
		
		if(this.pricings!=null) for(Pricing item: this.pricings) item.setShop(id);
		if(this.whoAreWe!=null) this.whoAreWe.setId(id);
	}
	public void setBackground(String background) throws Exception	{
		this.background = Util.getInstance().saveImage(background);
	}
	public void setLogo(String logo) throws Exception{
		this.logo = Util.getInstance().saveImage(logo);
	}
	
	public void setGallery(List<Media> gallery) {
		this.gallery = gallery;
	}
	public void setLegalData(LegalData legalData) {
		if(legalData==null) return;
		this.legalData = legalData;		
		legalData.setId(getId());
	}
	/*CURRENCY*/
	public void setCurrencies(String ... currencies){
		if(currencies==null) return;
		
		String[] currs=getCurrencies();		
		for(String item:currencies){
			if(Arrays.binarySearch(currs, item)<0)	{
			pricings.add(new Pricing(getId(),"base", item).save());
			pricings.add(new Pricing(getId(),"web", item).save());
			}
		}		
		
	}

	public void addPricing(String name, String currency){
		if(id==null) save();
		this.pricings.add(new Pricing(id, name, currency));		
	}
	public Category delCategory(String name){
		for(Category item:this.categories)
			if(item.getId().equals(name) || item.getName().equals(name))
				{				
				this.categories.remove(item);
				return item;
				}
		return null;
	}
	public void addCategory(Category cat){
		for(Category item:this.categories)
			if(item.getId().equals(cat.getId()))
				{
				item.setName(cat.getName());
				return;
				}
		categories.add(cat);		
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
	public void setPricings(List<Pricing> pricings) {		
		for(Pricing item:pricings)			
			item.setShop(this.getId());
			
		this.pricings = pricings;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public Boolean getVisible() {
		return visible;
	}
	public void setVisible(Boolean visible) {
		this.visible = visible;
	}
	public Paypal getPaypal() {
		return paypal;
	}
	public void setPaypal(Paypal paypal) {
		this.paypal = paypal;
	}
	
	/*STAFF*/
	public void addStaff(String role, String mail)	{	
		if(mail==null) return;
		for(Staff item:this.staff)
			if(mail.equals(item.getMail()))
				return;
				
		this.staff.add(new Staff(mail,role));
	}
	public void removeStaff(String role, String mail)	{			
		for(Staff item:this.staff)
			if((item.getMail()==null && mail==null) || mail.equals(item.getMail()))
				{
				this.staff.remove(item);
				return;
				}
				
	}
	public List<Staff> getStaff() {
		return staff;
	}
	public void setStaff(List<Staff> staff) {
		this.staff = staff;
	}
	public List<String> getGoodTypes() {
		if(goodTypes.size()==0)
			return Arrays.asList(new String[]{"generic"});
			
		return goodTypes;
	}
	public void addGoodType(String goodType) {
		this.goodTypes.add(goodType);
	}
	public void setGoodTypes(List<String> goodTypes) {
		this.goodTypes = goodTypes;
	}
	/**gestione messaggistica**/
	public List<String> sendNotify(String from, String message,String type)	{
		List<String> users = new ArrayList<String>();
		for(Staff user:this.staff)
			users.add(user.getMail());
		
		HashMap map = new HashMap();
			map.put("from", from);
			map.put("to", this.id);
			map.put("type", type);
			map.put("target", "shop");
		
		
		try{OpenCommunity.getInstance().send("chat",message,map,users);}catch(Exception e){}		
		
		return users;
	}
	public void send(String from, String message)	{
		List<String> users = new ArrayList<String>();
		for(Staff user:this.staff)
			users.add(user.getMail());
		OpenCommunity.getInstance().addMessage(from,this.id,message);
		HashMap map = new HashMap();
			map.put("from", from);
			map.put("to", this.id);
			map.put("type", "chat.shop");
			
		OpenCommunity.getInstance().send("chat",message,map,users);
	}
	public void answer(String to, String message)	{		
		OpenCommunity.getInstance().addMessage(this.id,to,message);
		HashMap map = new HashMap();
			map.put("from", this.id);
			map.put("to", to);
			map.put("type", "chat.user");
			
		OpenCommunity.getInstance().send("chat",message,map,to);
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
	public void setStyle(String style) {
		this.style = style;
	}
	
	public void send(String title, String text, String link)
		{
		
		}
		
	
	public void save(Category cat){
		Repositories.category.save(cat);
	}
	public void save(Item item){
		Repositories.item.save(item);
	}
		
	public void save(){				
		
		Repositories.shop.save(this);		
	}
	
	static public class Mail
		{
		public String from;
		public String subject;
		public String template;
		}
	
	}
