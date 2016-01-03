package org.index.repository;

import org.index.categories.GoodClassRepository;
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
			ShopRepository shop,
			GoodClassRepository goodClass,
			StaffRepository staff
			)
		{		
		Repositories.pricing	=	pricing;		
		Repositories.item		=	item;
		Repositories.category	=	category;
		Repositories.catalogue	= 	catalogues;
		Repositories.shop		= 	shop;
		Repositories.goodClass	=	goodClass;
		Repositories.staff		=	staff;
		}

	static public PricingRepository pricing;
	static public ItemRepository item;
	static public CategoryRepository category;
	static public CatalogueRepository catalogue;
	static public ShopRepository shop;
	static public GoodClassRepository goodClass;
	static public StaffRepository staff;
}
