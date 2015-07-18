package org.index;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.sun.mail.util.BASE64DecoderStream;

@PropertySource({"index.properties"})
@Component
public class Util {
	@Value("${index.img.root}") private String root;
	@Value("${index.img.url}") private String url;
	
	
	static private Util instance;
	static public Util getInstance()
		{
		return instance;
		}
	@PostConstruct
	public void init()
		{
		instance=this;
		}
	
	public String saveImage(String img) throws Exception
		{
		return saveImage(img,null);
		}
	
	public InputStream getImage(String img) throws Exception
		{		
	    return new FileInputStream(new File(root,img));
		}
	public String saveImage(String img, String name) throws Exception
		{		
		File rootImage = new File(root);		
		if(img.startsWith("data:"))
			{
			int pos = img.indexOf(",");
			String prefix = img.substring(0,pos);
			String data = img.substring(pos+1);			
			String imgtype=prefix.substring(prefix.indexOf("/")+1,prefix.indexOf(";"));
			
		    byte[] imgBytes = BASE64DecoderStream.decode(data.getBytes());		    
		    BufferedImage bufImg = ImageIO.read(new ByteArrayInputStream(imgBytes));
		    rootImage.mkdirs();
		    
		    if(name==null) name= UUID.randomUUID().toString();
		    File imgOutFile = new File(rootImage,name+"."+imgtype);
		    ImageIO.write(bufImg, imgtype, imgOutFile);
		    		    
		    return url+imgOutFile.getName();
			}
		if(!img.matches(url))
			try{					
			System.out.println("download:"+img);
			URL ur = new URL(img);
			InputStream in = ur.openStream();
			
			if(name==null)	
				{
				name = ur.getFile();
				String[] path = name.split("/");
				name = path[path.length-1];
				}
				
			//String imgtype = name.split("\\.")[1];
			
			File imgOutFile = new File(rootImage,name);
				imgOutFile.getParentFile().mkdirs();
				imgOutFile.createNewFile();
			FileOutputStream out = new FileOutputStream(imgOutFile);
			IOUtils.copy(in, out);
			System.out.println("...download:"+url+imgOutFile.getName());
			return url+imgOutFile.getName();
			}catch (Exception e) {
				e.printStackTrace();
			}
		return img;
		}
	
	public static void main(String[] args)throws Exception {
		System.out.println(new URL("https://img.scalaplayhouse.com/products/3002454055/3002454055_136x192.jpg").getPath());
	}
}
