package org.index.repository;

import java.util.List;

import org.hibernate.annotations.Cache;
import org.index.obj.Pricing;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.Repository;



public interface PricingRepository extends Repository<Pricing, String> 
{
	@Cacheable("pricing")
	public List<Pricing> findByShop(String shop);
	public Pricing findByShopAndNameAndCurrency(String shop,String name,String currency);	
	public List<Pricing> findAll();
	public Pricing save(Pricing wsdl);
	@Cacheable("pricing")
	public Pricing findOne(String princing);	
	public void delete(String entity);
	public boolean exists(String entity);
}
