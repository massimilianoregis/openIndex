package org.index.obj;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;

import javax.imageio.ImageIO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.index.Util;
import org.index.service.IndexService.View;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.sun.mail.util.BASE64DecoderStream;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Media 
	{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	@JsonView(View.Shop.Full.class)
	private Long uid;
	@Column(columnDefinition="TEXT")
	@JsonView(View.Shop.Full.class)
	private String img;
	@JsonView(View.Shop.Full.class)
	private String type;
	
	public Media() {
	
	}
	public Media(String img) throws Exception{
		setSrc(img);
	}
	public Long getUid() {
		return uid;
	}
	public void setUid(Long uid) {
		this.uid = uid;
	}
	public String getSrc() {
		return img;
	}
	public String getImg() {
		return img;
	}
	public String getType()
		{
		return type;
		}
	public void setSrc(String img) throws Exception 
		{					
		this.img = Util.getInstance().saveImage(img);
		this.type="image";
		}
	public void setImg(String img) throws Exception 
		{
		this.img = Util.getInstance().saveImage(img);
		this.type="image";
		}
	
	}
