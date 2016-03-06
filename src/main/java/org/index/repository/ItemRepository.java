package org.index.repository;

import java.util.List;

import org.index.obj.Category;
import org.index.obj.Item;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.Repository;



public interface ItemRepository extends Repository<Item, String> 
{
	public List<Item> findAll();
	public List<Item> findByShop(String shop);
	public List<Item> findByVisible(boolean visibility);
	public List<Item> findByShopAndCategoriesId(String shop,String id);
	
	public List<Item> findByShopAndCataloguesRestrictFalse(String shop,String id);
	@Cacheable("item")
	public List<Item> findByShopAndCataloguesId(String shop,String id);
	public List<Item> findByShopAndCataloguesIdAndCategoriesId(String shop,String catalogue, String category);
	
	public Item save(Item wsdl);
	public Item findOne(String mail);
	
	public void delete(String entity);
	public boolean exists(String entity);
}
