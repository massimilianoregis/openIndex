package org.index.repository;

import java.util.List;

import org.index.paintings.Author;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.Repository;



public interface AuthorRepository extends Repository<Author, String> 
{
	//@Cacheable("author")
	public List<Author> findAll();
	public Author save(Author wsdl);
	//@Cacheable("author")
	public Author findOne(String mail);
	public List<Author> findByFirstNameAndLastName(String firstName,String lastName);
	public void delete(String entity);
	public boolean exists(String entity);
}
