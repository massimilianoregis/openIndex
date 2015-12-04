package org.index.repository;

import java.util.List;

import org.index.obj.Page;
import org.springframework.data.repository.Repository;



public interface PageRepository extends Repository<Page, String> 
{
	public List<Page> findAll();
	
	public Page save(Page shop);
	public Page findOne(String id);
	
	public void delete(Page entity);
	public boolean exists(String entity);
}
