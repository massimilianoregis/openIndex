package org.index.obj;

import java.util.ArrayList;
import java.util.List;

import org.index.repository.Repositories;
import org.opencommunity.objs.User;
import org.opencommunity.security.AccessControl;
import org.springframework.cache.annotation.Cacheable;

public class Shop {
	private String name;
	public Shop(String name){
		this.name=name;
	}
	
	public List<Pricing> getPricings(){
		return Repositories.pricing.findByShop(name);
	}
	public List<Catalogue> getCatalogues(){
		return Repositories.catalogue.findByShop(name);
	}
	public List<Category> getCategories(){
		return Repositories.category.findByShop(name);
	}
	public List<Item> getItems(){
		return Repositories.item.findByShop(name);
	}
		
	public List<Item> getItems(String catalogue){		
		//if(!Repositories.catalogue.findOne(catalogue).isRestrict())
		//	return Repositories.item.findByShopAndCataloguesId(this.name,catalogue);
		
		User user = AccessControl.getUser();
		if(user==null) return new ArrayList<Item>();
		List<String> list = (List)user.getData().get("catalogue");
		
		if(!list.contains(catalogue)) return new ArrayList<Item>();		
		return Repositories.item.findByShopAndCataloguesId(this.name,catalogue);
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
