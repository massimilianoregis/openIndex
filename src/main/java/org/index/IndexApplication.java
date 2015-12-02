package org.index;


import java.awt.Desktop;
import java.net.URI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@Configuration

@ComponentScan(basePackages="org.index",includeFilters=@ComponentScan.Filter(value= org.index.filter.AllowOriginFilter.class, type=FilterType.ASSIGNABLE_TYPE))
@EnableAutoConfiguration(exclude = JpaRepositoriesAutoConfiguration.class)
//@EnableAspectJAutoProxy
//@EnableJpaRepositories(basePackages="org.index")
//@EnableSpringConfigured
public class IndexApplication extends SpringBootServletInitializer 
{
	@Override
	 protected SpringApplicationBuilder configure(SpringApplicationBuilder application) 
		{
		return application.sources(IndexApplication.class);
		}
	
	@Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("item");
    }
    public static void main(String[] args) throws Exception{
    //	System.setProperty("spring.profiles.default", System.getProperty("spring.profiles.default", "dev"));
        ConfigurableApplicationContext ctx =SpringApplication.run(IndexApplication.class, args);
//		Item item = new Item("A1","A1");
//		item.addCategory("alimentari");
//		item.setPrice("shop", 100F);
//	System.out.println(new ObjectMapper().writeValueAsString(item));
        Desktop.getDesktop().browse(new URI("http://localhost:8080/index.html"));
       
    }
}