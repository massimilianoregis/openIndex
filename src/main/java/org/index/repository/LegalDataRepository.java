package org.index.repository;

import java.util.List;

import org.index.obj.LegalData;
import org.springframework.data.repository.Repository;



public interface LegalDataRepository extends Repository<LegalData, String> 
{
	public List<LegalData> findAll();
	
	public LegalData save(LegalData shop);
	public LegalData findOne(String id);
	
	public void delete(LegalData entity);
	public boolean exists(String entity);
}
