package org.index.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("indexRepositories")
public class Repositories {
	@Autowired
	public void setRepositories(
			PricingRepository pricing,			
			ItemRepository item,
			CategoryRepository category,
			CatalogueRepository catalogues,
			ShopRepository shop
			)
		{		
		Repositories.pricing	=	pricing;		
		Repositories.item		=	item;
		Repositories.category	=	category;
		Repositories.catalogue	= 	catalogues;
		Repositories.shop		= 	shop;
		}

	static public PricingRepository pricing;
	static public ItemRepository item;
	static public CategoryRepository category;
	static public CatalogueRepository catalogue;
	static public ShopRepository shop;
}
