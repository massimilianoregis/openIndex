package org.index.repository;

import java.util.List;

import org.index.obj.Category;
import org.index.obj.Item;
import org.index.obj.Shop;
import org.index.obj.Staff;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.Repository;



public interface ShopRepository extends Repository<Shop, String> 
{
	public List<Shop> findAll();
	
	public Shop save(Shop shop);
	public Shop findOne(String id);
	public List<Shop> findByStaffMail(String mail);
	
	public void delete(Shop entity);
	public boolean exists(String entity);
}
