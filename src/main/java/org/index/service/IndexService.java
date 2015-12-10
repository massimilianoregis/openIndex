package org.index.service;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.index.Index;
import org.index.Util;
import org.index.categories.GoodClass;
import org.index.obj.Catalogue;
import org.index.obj.Category;
import org.index.obj.Item;
import org.index.obj.Pricing;
import org.index.obj.Shop;
import org.index.obj.Wear;
import org.index.repository.Repositories;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@Transactional("indexTransactionManager")
@RequestMapping(value="/index")
public class IndexService 
{
	@Autowired
	private Index index;

	
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
	@RequestMapping(value={"/category","/category/{id}"},method=RequestMethod.POST)
	public @ResponseBody void addCategory(@RequestBody Category group) throws Exception
		{		
		index.getShop(group.getShop()).addCategory(group);
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
	@RequestMapping(value="/shop/test",method=RequestMethod.GET)
	public @ResponseBody Shop newShop() throws Exception
		{						
		Shop shop = new Shop("Star wars");					
			shop.setBackground("http://im.ziffdavisinternational.com/t/ign_it/screenshot/default/1429211669-star-wars-battlefront-key-art_hbw8.1920.jpg");			
			shop.setCurrencies("USD","EUR");
			shop.addCategories("Oggetti","Quadri");
		shop.save();
		
			
		{
		System.out.println("primo prodotto");
		Item item = shop.newItem();				
			item.setName("Spada Laser");
			item.addImage("http://www.artsfon.com/pic/201501/1366x768/artsfon.com-45936.jpg");			
			item.setCategories("Oggetti");						
			item.setPrices(new Item.Price(shop.getId(),"base","EUR",100D),
							new Item.Price(shop.getId(),"base","USD",100D),
							new Item.Price(shop.getId(),"web","EUR",80D));
			item.save();
		
		System.out.println("/primo prodotto");
		}
		
		{
		System.out.println("secondo prodotto");
		Item item = shop.newItem();				
			item.setName("Force Friday");
			item.addImage("http://pixel.nymag.com/imgs/daily/vulture/2015/09/04/force-fridays/bb8.nocrop.w529.h560.png");			
			item.setCategories("Oggetti");	
			item.setPrices(new Item.Price(shop.getId(),"base","EUR",100D),
							new Item.Price(shop.getId(),"base","USD",100D),
							new Item.Price(shop.getId(),"web","EUR",80D));
			item.save();
		
		System.out.println("/secondo prodotto");
		}
		
		{
		System.out.println("terzo prodotto");
		Item item = shop.newItem();				
			item.setName("Toast");
			item.setCategories("Oggetti");
			item.addImage("https://s-media-cache-ak0.pinimg.com/236x/ce/83/0b/ce830bb484e4809a8544a6760e7ae111.jpg");
			item.setPrices(new Item.Price(shop.getId(),"base","EUR",100D),
							new Item.Price(shop.getId(),"base","USD",100D),
							new Item.Price(shop.getId(),"web","EUR",80D));
		item.save();
		System.out.println("/terzo prodotto");
		}
		
		{
		System.out.println("quarto prodotto");
		Item item = shop.newItem();				
			item.setName("Coffee");
			item.setCategories("Oggetti");
			item.addImage("http://cnet1.cbsistatic.com/hub/i/r/2012/12/07/05a1a3d0-fdc7-11e2-8c7c-d4ae52e62bcc/resize/570xauto/a20b9cea9511045cbdedaa50e63009fd/Crave27.jpg");
			item.setPrices(new Item.Price(shop.getId(),"base","EUR",100D),
							new Item.Price(shop.getId(),"base","USD",100D),
							new Item.Price(shop.getId(),"web","EUR",80D));
			item.save();
		System.out.println("/quarto prodotto");
		}
			
		return shop;
		}
	@RequestMapping(value="/shop",method=RequestMethod.GET)
	public @ResponseBody List<Shop> getShops() throws Exception
		{				
		return index.getShops();
		}
	@RequestMapping(value="/shop/{shop}",method=RequestMethod.GET)
	public @ResponseBody Shop getShops(@PathVariable String shop) throws Exception
		{				
		return index.getShop(shop);
		}
	@RequestMapping(value={"/shop","/shop/{id}"},method=RequestMethod.POST)
	public @ResponseBody Shop addShop(@RequestBody(required=false) Shop shop,@RequestParam(required=false) String name) throws Exception
		{
		if(shop==null) shop=new Shop(name);
		if(shop==null && name==null) return null;
		return index.addShop(shop);
		}
	@RequestMapping(value="/shop/{shop}",method=RequestMethod.DELETE)
	public @ResponseBody void deleteShop(@PathVariable String shop) throws Exception
		{				
		index.deleteShop(shop);
		}
	
	/**Prodotti**/
	@RequestMapping(value="/item",method=RequestMethod.GET)
	public @ResponseBody List<Item> getItems(@RequestParam(required=false) String shop) throws Exception
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
		
	@RequestMapping(value="/item/{id}",method=RequestMethod.GET)
	public @ResponseBody Item getItem(@PathVariable String id) throws Exception
		{				
		return index.getItem(id);
		}
	
	@RequestMapping(value="/{shop}/items/{id}",method=RequestMethod.GET)
	public @ResponseBody List<Item> getItemGroup(@PathVariable String shop,@PathVariable String id) throws Exception
		{							
		System.out.println(shop+" "+id);
		return index.getItemGroup(shop,shop+"."+id);
		}
	@RequestMapping(value={"/item","/item/{id}"},method=RequestMethod.POST)
	public @ResponseBody void addItem(@RequestBody Item item) throws Exception
		{			
		index.addItem(item);		
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

	
	
}
