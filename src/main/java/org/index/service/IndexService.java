package org.index.service;

import java.util.List;

import org.apache.commons.io.IOUtils;
import org.index.Index;
import org.index.Util;
import org.index.obj.Catalogue;
import org.index.obj.Category;
import org.index.obj.Item;
import org.index.obj.Pricing;
import org.index.obj.Shop;
import org.index.obj.Wear;
import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
@Transactional("indexTransactionManager")
@RequestMapping(value="/index")
public class IndexService 
{
	@Autowired
	private Index index;

	
	/**Immagini dei prodotti**/
	@RequestMapping(value="/image/{name:.+}",method=RequestMethod.GET)
	public ResponseEntity<byte[]> getImage(@PathVariable String name) throws Exception
		{
		final HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.IMAGE_PNG);

	    return new ResponseEntity<byte[]>(IOUtils.toByteArray(Util.getInstance().getImage(name)), headers, HttpStatus.CREATED);
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
	@RequestMapping(value="/category",method=RequestMethod.POST)
	public @ResponseBody void addCategory(@RequestBody Category group) throws Exception
		{		
		System.out.println("add category "+group);
		index.addCategory(group);
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
	
	/**Negozi**/
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
	@RequestMapping(value="/shop",method=RequestMethod.POST)
	public @ResponseBody void addShop(@RequestBody(required=false) Shop shop, String name) throws Exception
		{
		if(shop==null) shop=new Shop(name);
		if(shop==null && name==null) return;
		index.addShop(shop);
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
