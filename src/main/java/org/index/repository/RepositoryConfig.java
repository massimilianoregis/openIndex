package org.index.repository;


import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

//@PropertySource({"index.properties"})
@Configuration
@Component("repositoryConfig")
@EnableJpaRepositories(
		basePackages = "org.index",
		entityManagerFactoryRef="entityManagerIndexFactory",
		transactionManagerRef="indexTransactionManager"
		)
public class RepositoryConfig {
	
	private DriverManagerDataSource datasource;
	@Value("${index.db.url}") private String dburl;
	@Value("${index.db.driver}") private String dbDriver;
	@Value("${index.db.user}") private String dbUser;
	@Value("${index.db.psw}") private String dbPsw;
	@Value("${index.db.dialect}") private String dbDialect;
	@Value("${index.db.create.url}") private String createUrl;
	@Value("${index.db.create.cmd}") private String createCmd;
	@Value("${index.img.root}") private String root;
	@Value("${index.img.url}") private String url;
	
	
	 @Bean
     public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() throws IOException {
         PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
         propertySourcesPlaceholderConfigurer.setIgnoreUnresolvablePlaceholders(Boolean.TRUE);
         try{
         if(InetAddress.getLocalHost().getHostAddress().equals("95.110.224.34"))
        	 propertySourcesPlaceholderConfigurer.setLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:remote/*.properties"));
         else
        	 propertySourcesPlaceholderConfigurer.setLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:local/*.properties"));
         }catch(Exception e)
         {
        	 propertySourcesPlaceholderConfigurer.setLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:local/*.properties"));
         }
         
         return propertySourcesPlaceholderConfigurer;
     }
	
	public DataSource dataSource() {
		  	//System.out.println("datasource:"+servletContext.getRealPath("WEB-INF/data"));
		 System.out.println("datasource-->"+this.dburl);
		datasource = new DriverManagerDataSource();
        datasource.setDriverClassName(dbDriver);
        datasource.setUrl(dburl);
        datasource.setUsername(dbUser);
        datasource.setPassword(dbPsw);	
		try	{
			datasource.getConnection();
			}
        catch(Exception e)
        	{        
        	DriverManagerDataSource newdatasource = new DriverManagerDataSource();
        	newdatasource.setDriverClassName(dbDriver);
        	newdatasource.setUrl(createUrl);
        	newdatasource.setUsername(dbUser);
        	newdatasource.setPassword(dbPsw);
        	Statement statement = null; 
        	try	{
        		statement = newdatasource.getConnection().createStatement();
        		statement.execute(createCmd);
        		statement.close();
        		}
        	catch(Exception ex)
        		{        		
        		ex.printStackTrace();
        		}
        	}
	  	
//	    	LocalSessionFactoryBuilder cnf =new LocalSessionFactoryBuilder(datasource);			
//			cnf.scanPackages("*");
//			cnf.getProperties().setProperty(org.hibernate.cfg.Environment.DIALECT, this.dbDialect);
//			new SchemaExport(cnf).execute(false, true, false,true);	
		System.out.println("/datasource");
	        return datasource;
	    }
	  

	  @Bean 
	  public LocalContainerEntityManagerFactoryBean entityManagerIndexFactory() {
		 System.out.println("\n\n*****************\nIndex\nrepositoryConfig\nEntity Manager\n**********************\n\n");
		  JpaVendorAdapter vendorAdapter = jpaVendorAdapter(null);
		  LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		    
		  Properties jpaProperties = new Properties();
		    		 jpaProperties.setProperty("hibernate.hbm2ddl.auto", "update");
		    
		  factory.setJpaProperties(jpaProperties);
		  factory.setJpaVendorAdapter(vendorAdapter);
		  factory.setPackagesToScan("org.index");
		  factory.setDataSource(dataSource());
		  factory.setPersistenceUnitName("index");
		  

		  factory.afterPropertiesSet();

		  return factory;
		  }
	  
//	 @Bean 
//	  public EntityManagerFactory entityManagerFactory() {
//		 System.out.println("\n\n*****************\nBase\nrepositoryConfig\nEntity Manager\n**********************\n\n");
//		  JpaVendorAdapter vendorAdapter = jpaVendorAdapter(null);
//		  LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
//		    
//		  Properties jpaProperties = new Properties();
//		    		 jpaProperties.setProperty("hibernate.hbm2ddl.auto", "update");
//		    
//		  factory.setJpaProperties(jpaProperties);
//		  factory.setJpaVendorAdapter(vendorAdapter);
//		  factory.setPersistenceUnitName("default");
//		  factory.setDataSource(dataSource());
//		  factory.setPackagesToScan("or.index");
//
//		  factory.afterPropertiesSet();
//
//		  return factory.getObject();
//		  }
	  
    public JpaVendorAdapter jpaVendorAdapter(final Environment environment) 
		{	
		System.out.println("jpaVendorAdapter");
		final HibernateJpaVendorAdapter rv = new HibernateJpaVendorAdapter();
		
	//	rv.setDatabase(Database.H2);
		//rv.setDatabasePlatform(H2Dialect.class.getName());
		
		rv.setDatabasePlatform(this.dbDialect);
		
		rv.setGenerateDdl(true);
		rv.setShowSql(true);
		
		return rv;
	    }
	
    @Bean(name = "indexTransactionManager")
    public PlatformTransactionManager transactionManager(){
    	JpaTransactionManager transactionManager = new JpaTransactionManager();
    	
    	transactionManager.setEntityManagerFactory(entityManagerIndexFactory().getObject());
    	
    	return transactionManager;
    	
    }

public static void main(String[] args) throws Exception{
	System.out.println(InetAddress.getLocalHost().getHostAddress());
}
}
