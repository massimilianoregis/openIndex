package org.index.repository;

import java.util.List;

import org.index.obj.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;



public interface CategoryRepository extends Repository<Category, String> 
{
	public List<Category> findByShop(String shop);
	public List<Category> findAll();
	public Category save(Category category);
	public Category findOne(String cat);
	public Category findByShopAndName(String shop, String cat);
	public void delete(String entity);
	public boolean exists(String entity);
}
