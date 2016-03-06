package org.index.repository;

import java.util.List;

import org.index.obj.Wear;
import org.springframework.data.repository.Repository;



public interface WearRepository extends Repository<Wear, String> 
{
	public List<Wear> findAll();
	public Wear save(Wear wsdl);
	public Wear findOne(String mail);
	public void delete(String entity);
	public boolean exists(String entity);
}
