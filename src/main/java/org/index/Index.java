package org.index;

import java.util.List;

import org.index.obj.Catalogue;
import org.index.obj.Category;
import org.index.obj.Item;
import org.index.obj.Pricing;
import org.index.obj.Shop;
import org.index.obj.Wear;
import org.index.repository.Repositories;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;


public class Index 
	{		
	
	
	/*shop*/
	public List<Shop> getShops()
		{
		return Repositories.shop.findAll();
		}
	public Shop getShop(String id)
		{
		Shop shop= Repositories.shop.findOne(id);
			
		return shop;
		}
	public Shop addShop(Shop shop)
		{
		return Repositories.shop.save(shop);
		}
	public void deleteShop(String id)
		{
		Repositories.shop.delete(getShop(id));
		}
	
	/*item*/
	public void deleteItem(String id)
		{
		Repositories.item.delete(id);
		}
	public void addItem(Item item)
		{
		Repositories.item.save(item);
		}
	public void addWear(Wear item)
		{
		//this.items.add(item);		
		Repositories.item.save(item);
		}
	
	public List<Pricing> getPricings()
		{
		return Repositories.pricing.findAll();
		}
	public Item addItem(String code, String name)
		{
		Item item = new Item(code,name);
		return item;
		}
	
	public List<Item> getItems()
		{
		//return Repositories.item.findByVisible(true);
		return Repositories.item.findAll();
		}
	public Item getItem(String id)
		{
		return Repositories.item.findOne(id);
		}
	public List<Item> getItemGroup(String shop,String id)
		{
		return Repositories.item.findByShopAndCategoriesId(shop, id);
		}
	
	public Pricing addPricing(Pricing cat)
		{			
		Repositories.pricing.save(cat);
		return cat;
		}
	
	public Category addCategory(String name)
		{
		Category cat = new Category(name,"");
		Repositories.category.save(cat);
		return cat;
		}
	public Category addCategory(Category cat)
		{		
		System.out.println(Repositories.category);
		Repositories.category.save(cat);
		return cat;
		}
	public List<Category> getCategories()
		{
		return Repositories.category.findAll();
		}
	public Category getCategory(String name)
		{
		return Repositories.category.findOne(name);
		}
	
	
	public Catalogue addCatalogue(String name)
		{
		Catalogue cat = new Catalogue(name);
		Repositories.catalogue.save(cat);
		return cat;
		}
	public Catalogue addCatalogue(Catalogue cat)
		{				
		Repositories.catalogue.save(cat);
		return cat;
		}
	public List<Catalogue> getCatalogues()
		{
		return Repositories.catalogue.findAll();
		}
	public Catalogue getCatalogue(String name)
		{
		return Repositories.catalogue.findOne(name);
		}
	}
