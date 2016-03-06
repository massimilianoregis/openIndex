package org.index.service;

import java.util.List;

import org.index.Index;
import org.index.obj.Item;
import org.index.paintings.Author;
import org.index.paintings.Painting;
import org.index.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Transactional("indexTransactionManager")
public class ArtService {
	@Autowired
	AuthorRepository authors;
	@Autowired
	private Index index;
	
	@RequestMapping(value="/art/author",method=RequestMethod.GET)
	public @ResponseBody List<Author> getAuthors() throws Exception {
		return authors.findAll();
	}
	@RequestMapping(value={"/art/author","/art/author/{id}"},method=RequestMethod.POST)
	public @ResponseBody void saveAuthor(@RequestBody Author author) throws Exception {
		authors.save(author);
	}
	@RequestMapping(value={"/art/author/{id}"},method=RequestMethod.GET)
	public @ResponseBody Author getAuthor(@PathVariable String id) throws Exception {
		return authors.findOne(id);
	}
	@RequestMapping(value={"/art/author/{firstName}/{lastName}"},method=RequestMethod.GET)
	public @ResponseBody List<Author> getAuthor(@PathVariable String firstName,@PathVariable String lastName) throws Exception {
		return authors.findByFirstNameAndLastName(firstName,lastName);
	}
	
	@RequestMapping(value={"/index/painting","/index/painting/{id}"},method=RequestMethod.POST)
	public @ResponseBody void addItem(@RequestBody Painting item) throws Exception
		{	
		try{
		index.addItem(item);
		
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		}
	
}
