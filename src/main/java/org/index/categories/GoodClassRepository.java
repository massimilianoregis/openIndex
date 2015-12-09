package org.index.categories;

import java.util.List;

import org.springframework.data.repository.Repository;



public interface GoodClassRepository extends Repository<GoodClass, String> 
{	
	public List<GoodClass> findAll();
	public GoodClass save(GoodClass category);
	public void delete(String entity);
	public boolean exists(String entity);
	
	public List<GoodClass> findByParentId(String id);
}
