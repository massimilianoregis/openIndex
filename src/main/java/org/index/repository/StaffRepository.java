package org.index.repository;

import java.util.List;

import org.index.obj.Shop;
import org.index.obj.Staff;
import org.springframework.data.repository.Repository;



public interface StaffRepository extends Repository<Staff, String> 
{
	public List<Staff> findAll();
	public Staff save(Staff wsdl);
	public Staff findOne(String mail);		
	public void delete(String entity);
	public boolean exists(String entity);
}
