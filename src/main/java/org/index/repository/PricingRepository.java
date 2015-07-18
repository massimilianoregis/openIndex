package org.index.repository;

import java.util.List;

import org.index.obj.Pricing;
import org.springframework.data.repository.Repository;



public interface PricingRepository extends Repository<Pricing, String> 
{
	public List<Pricing> findByShop(String shop);
	public List<Pricing> findAll();
	public Pricing save(Pricing wsdl);
	public Pricing findOne(String princing);	
	public void delete(String entity);
	public boolean exists(String entity);
}
