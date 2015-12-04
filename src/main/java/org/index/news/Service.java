package org.index.news;

import java.util.List;

import org.index.Index;
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
@RequestMapping("/index/{shop}/news")
public class Service {
	@Autowired
	private Index index;
	
	@RequestMapping(value="",method=RequestMethod.POST)
	public @ResponseBody void addNews(@RequestBody News news,@PathVariable String shop)
		{
		index.getShop(shop).addNews(news);
		}
	
	@RequestMapping(value="",method=RequestMethod.GET)
	public @ResponseBody List<News> getNews(@PathVariable String shop)
		{
		return index.getShop(shop).getLatestNews();
		}
	
	@RequestMapping(value="/test",method=RequestMethod.GET)
	public @ResponseBody void addNews(String title, String text,@PathVariable String shop)
		{
		index.getShop(shop).addNews(new News(title,text));
		}
}
