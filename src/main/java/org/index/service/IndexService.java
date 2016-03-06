package org.index.service;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.index.Index;
import org.index.Util;
import org.index.categories.GoodClass;
import org.index.obj.Catalogue;
import org.index.obj.Category;
import org.index.obj.Item;
import org.index.obj.Pricing;
import org.index.obj.Shop;
import org.index.obj.Staff;
import org.index.obj.Wear;
import org.index.repository.Repositories;
import org.opencommunity.security.AccessControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@Transactional("indexTransactionManager")
@RequestMapping(value="/index")
public class IndexService 	
{
	@Autowired
	private Index index;	
	
	static class Prova
		{
		private String name;
		public Prova(String name){this.name=name;}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}		
		
		}

	/**Immagini dei prodotti**/	
	@RequestMapping(value="/image/{name:.+}",method=RequestMethod.GET)
	public ResponseEntity<byte[]> getImage(@PathVariable String name,@RequestParam(required=false) Integer w,@RequestParam(required=false) Integer h) throws Exception
		{
		final HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.IMAGE_JPEG);

	    return new ResponseEntity<byte[]>(IOUtils.toByteArray(Util.getInstance().getImage(name,w,h)), headers, HttpStatus.CREATED);
		}
	
	
	/**Tipologia di prezzi**/
	@RequestMapping(value="/pricing",method=RequestMethod.GET)
	public @ResponseBody List<Pricing> getPricing(@RequestParam(required=false) String shop)
		{
		if(shop==null) return index.getPricings();
		return index.getShop(shop).getPricings();
		}
	@RequestMapping(value="/pricing/{shop}",method=RequestMethod.GET)
	public @ResponseBody List<Pricing> getPricingByShop(@PathVariable String shop)
		{		
		return index.getShop(shop).getPricings();
		}
		
	@RequestMapping(value="/pricing",method=RequestMethod.POST)
	public @ResponseBody void addPricing(@RequestBody Pricing pricing)
		{
		System.out.println("pricing:"+pricing);
		index.addPricing(pricing);
		}
	
	/**Catalogue**/
	@RequestMapping(value="/catalogue/{name:.+}",method=RequestMethod.POST)
	public @ResponseBody void saveCatalogue(@RequestBody Catalogue group) throws Exception
		{		
		System.out.println("add category "+group);
		index.addCatalogue(group);
		}
	@RequestMapping(value="/catalogue",method=RequestMethod.POST)
	public @ResponseBody void addCatalogue(@RequestBody Catalogue group) throws Exception
		{		
		System.out.println("add category "+group);
		index.addCatalogue(group);
		}
	@RequestMapping(value="/catalogue",method=RequestMethod.GET)
	public @ResponseBody List<Catalogue> getCatalogue(@RequestParam(required=false) String shop) throws Exception
		{		
		if(shop==null)	return index.getCatalogues();
		return index.getShop(shop).getCatalogues();
		}
	@RequestMapping(value="/catalogue/{name}",method=RequestMethod.DELETE)
	public @ResponseBody void deleteCatalogue(@PathVariable String name) throws Exception
		{		
		index.getCategory(name).remove();
		}
	
	/**Gruppi**/
	@RequestMapping(value="/item/{item}/group/{name}",method=RequestMethod.GET)
	public @ResponseBody Item addItemGroup(@PathVariable String item,@PathVariable String name) throws Exception
		{
		Item itm = index.getItem(item);
			itm.addCategory(name);
		return itm;
		}
	@RequestMapping(value="/shop/{shop}/groupReset",method=RequestMethod.GET)
	public @ResponseBody Shop addGroup(@PathVariable String shop) throws Exception
		{						
		try{
			while(true)
				index.getShop(shop).getCategories().remove(0);
			}
		catch(Exception e)
			{
			return index.getShop(shop);	
			}				
		}
	@RequestMapping(value="/shop/{shop}/group/{name}",method=RequestMethod.GET)
	public @ResponseBody Shop addGroup(@PathVariable String shop,String to,@PathVariable String name) throws Exception
		{						
		index.getShop(shop).addCategory(new Category(name,shop));
		return index.getShop(shop);
		}
	@RequestMapping(value={"/category","/category/{id}"},method=RequestMethod.POST)
	public @ResponseBody Category addCategory(@RequestBody Category group) throws Exception
		{		
		if(group.getName().equals(group.getId())) group.setId(UUID.randomUUID().toString());
		index.getShop(group.getShop()).addCategory(group);
		return group;
		}
	@RequestMapping(value="/category",method=RequestMethod.GET)
	public @ResponseBody List<Category> getCategories(@RequestParam(required=false) String shop) throws Exception
		{		
		if(shop==null)	return index.getCategories();
		return index.getShop(shop).getCategories();
		}
	@RequestMapping(value="/category/{shop}",method=RequestMethod.GET)
	public @ResponseBody List<Category> getCategoriesByShop(@PathVariable String shop) throws Exception
		{				
		return index.getShop(shop).getCategories();
		}
	@RequestMapping(value="/category/{name}",method=RequestMethod.DELETE)
	public @ResponseBody void deleteCategories(@PathVariable String name) throws Exception
		{		
		index.getCategory(name).remove();
		}
	/**GoodClasses**/
	@RequestMapping(value="/class",method=RequestMethod.GET)
	public @ResponseBody List<GoodClass> getClasses(){
		return Repositories.goodClass.findByParentId(null);
		}
	@RequestMapping(value="/class",method=RequestMethod.PUT)
	public @ResponseBody List<GoodClass> setClasses(List<GoodClass> classes){
		for(GoodClass item:classes)
			Repositories.goodClass.save(item);
		return getClasses();
		}
	@RequestMapping(value="/class/default",method=RequestMethod.GET)
	public @ResponseBody List<GoodClass> setClassesDefault() throws Exception{
		InputStream json = this.getClass().getClassLoader().getResourceAsStream("goodClasses.json");
		GoodClass[] cls= new ObjectMapper().readValue(json, GoodClass[].class);
		return setClasses(Arrays.asList(cls));
		}
	
	
	/**Negozi**/
	
	@Cacheable("shops")
	@JsonView(View.Shop.Base.class)
	@RequestMapping(value="/shop",method=RequestMethod.GET)
	public @ResponseBody List<Shop> getShops() throws Exception
		{						
		return index.getShops();
		}
	@JsonView(View.Shop.Full.class)
	@RequestMapping(value="/shop/{shop}",method=RequestMethod.GET)
	public @ResponseBody Shop getShops(@PathVariable String shop) throws Exception
		{				
		return index.getShop(shop);
		}
	@CacheEvict(value = "shops", allEntries = true)
	@RequestMapping(value={"/shop","/shop/{id}"},method=RequestMethod.POST)	
	public @ResponseBody Shop addShop(@RequestBody(required=false) Shop shop,@RequestParam(required=false) String name) throws Exception
		{
		if(shop==null && name==null) return null;
		if(shop==null) 	shop=new Shop(name);
		//else			shop = index.getShop(shop.getId());		
		Shop sh =index.addShop(shop);
		sh.addStaff("owner",AccessControl.getUser().getMail());
		return sh;
		}
	@CacheEvict(value = "shops", allEntries = true)
	@RequestMapping(value="/shop/{shop}",method=RequestMethod.DELETE)
	public @ResponseBody void deleteShop(@PathVariable String shop) throws Exception
		{				
		index.deleteShop(shop);
		}
	


	@RequestMapping(value="/shop/{shop}/visible",method=RequestMethod.GET)
	public @ResponseBody Shop visible(@PathVariable String shop) throws Exception
		{				
		Shop sh = index.getShop(shop);
			 sh.setVisible(true);
			 sh.save();
		return sh;
		}
	@RequestMapping(value="/shop/{shop}/invisible",method=RequestMethod.GET)
	public @ResponseBody Shop invisible(@PathVariable String shop) throws Exception
		{				
		Shop sh = index.getShop(shop);
			 sh.setVisible(false);
			 sh.save();
		return sh;
		}
	@RequestMapping(value="/shop/{shop}/owner",method=RequestMethod.GET)
	public @ResponseBody List<Staff> getOwner(@PathVariable String shop) throws Exception
		{				
		return index.getShop(shop).getStaff();
		}
	@RequestMapping(value="/shop/{shop}/owner/{mail:.*}",method=RequestMethod.GET)
	public @ResponseBody void addOwner(@PathVariable String shop,@PathVariable String mail) throws Exception
		{						
		index.getShop(shop).addStaff("owner", mail);
		}
	@RequestMapping(value="/shop/{shop}/owner/remove",method=RequestMethod.GET)
	public @ResponseBody void removeStaff(@PathVariable String shop,String mail) throws Exception
		{						
		index.getShop(shop).removeStaff("owner", mail);
		}
	@RequestMapping(value="/shop/{shop}/style",method=RequestMethod.GET)
	public @ResponseBody Shop setStyle(@PathVariable String shop,@RequestParam String link) throws Exception
		{						
		Shop sh = index.getShop(shop);
			 sh.setStyle(link);
			 sh.save();
		return sh;
		}
	
	
	@JsonView(View.Shop.Base.class)
	@RequestMapping(value="/owner/{mail:.*}",method=RequestMethod.GET)	
	public @ResponseBody List<Shop> getOwners(@PathVariable String mail) throws Exception
		{						
		return Repositories.shop.findByStaffMail(mail);		
		}
	@JsonView(View.Shop.Base.class)
	@RequestMapping(value="/owner",method=RequestMethod.GET)	
	public @ResponseBody List<Shop> getOwners2(String mail) throws Exception
		{						
		return Repositories.shop.findByStaffMail(mail);		
		}
	
	/**Gestione chat**/
	@RequestMapping(value="/shop/{shop}/send/{type}",method=RequestMethod.GET)
	public @ResponseBody List<String> sendNotify(@PathVariable String shop,@PathVariable String type,String from, String message) throws Exception
		{						
		return index.getShop(shop).sendNotify(from,message,type);
		}
	@RequestMapping(value="/shop/{shop}/send",method=RequestMethod.GET)
	public @ResponseBody void send(@PathVariable String shop,String from, String message) throws Exception
		{						
		index.getShop(shop).send(from,message);
		}
	@RequestMapping(value="/shop/{shop}/answer",method=RequestMethod.GET)
	public @ResponseBody void answer(@PathVariable String shop,String to, String message) throws Exception
		{						
		index.getShop(shop).answer(to,message);
		}
	
	
	
	
	/**Prodotti**/
	@RequestMapping(value="/item",method=RequestMethod.GET)
	public @ResponseBody List<Item> getItems(@RequestParam(required=false) String shop) throws Exception
		{		
		if(shop==null)	return index.getItems();
		return index.getShop(shop).getItems();
		}
	@RequestMapping(value="/shop/{shop}/items",method=RequestMethod.GET)
	public @ResponseBody List<Item> getItemsShop(@PathVariable String shop) throws Exception
		{		
		if(shop==null)	return index.getItems();
		return index.getShop(shop).getItems();
		}
	
	
	@RequestMapping(value="/item/catalogue",method=RequestMethod.GET)
	public @ResponseBody List<Item> getItemsCatalogue(@RequestParam(required=false) String shop,@RequestParam String catalogue) throws Exception
		{				
		String[] split = catalogue.split("\\.");
		
		if(shop==null && split.length==2)			
			shop = split[0];			
		
		return index.getShop(shop).getItems(catalogue);
		}
		
	@RequestMapping(value="/item/{id:.*}",method=RequestMethod.GET)
	public @ResponseBody Item getItem(@PathVariable String id) throws Exception
		{				
		return index.getItem(id);
		}
	
	@RequestMapping(value={"/{shop}/items/{id}","/shop/{shop}/items/{id}"},method=RequestMethod.GET)
	public @ResponseBody List<Item> getItemGroup(@PathVariable String shop,@PathVariable String id) throws Exception
		{							
		System.out.println(shop+" "+id);
		return index.getItemGroup(shop,shop+"."+id);
		}
	@RequestMapping(value={"/item","/item/{id}"},method=RequestMethod.POST)
	public @ResponseBody Item addItem(@RequestBody Item item) throws Exception
		{			
		index.addItem(item);
		return item;
		}
	@RequestMapping(value={"/item/{id:.*}"},method=RequestMethod.DELETE)
	public @ResponseBody void deleteItem(@PathVariable String id) throws Exception
		{			
		index.deleteItem(id);
		}
	@RequestMapping(value="/wear",method=RequestMethod.POST)
	public @ResponseBody void addWear(@RequestBody Wear item) throws Exception
		{	
		try{
		index.addItem(item);
		
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		}

	
	public static class View
		{
		public static class Shop
			{
			public static class Base	{}
			public static class Full extends Base {}	
			} 
		
		}
}
