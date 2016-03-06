package org.index.news;

import java.util.List;

import org.index.obj.Category;
import org.index.obj.Item;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.Repository;



public interface NewsRepository extends Repository<News, String> 
{
	public List<News> findAll();
		
	public News save(News wsdl);
	public News findOne(String mail);
	
	public void delete(String entity);
	public boolean exists(String entity);
}
