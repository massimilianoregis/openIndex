package org.index.repository;

import java.util.List;

import org.index.obj.Catalogue;
import org.springframework.data.repository.Repository;



public interface CatalogueRepository extends Repository<Catalogue, String> 
{	
	public List<Catalogue> findByShop(String shop);
	public List<Catalogue> findAll();
	public Catalogue save(Catalogue category);	
	public Catalogue findOne(String cat);
	public void delete(String entity);
	public boolean exists(String entity);
}
