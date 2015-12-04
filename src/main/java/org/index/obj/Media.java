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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.mail.util.BASE64DecoderStream;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Media 
	{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private Long uid;
	@Column(columnDefinition="TEXT")
	private String img;
	
	public Media() {
	
	}
	public Media(String img){
		this.img=img;
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
	public void setSrc(String img) throws Exception 
	{
	
	this.img = Util.getInstance().saveImage(img);
	
	}
	public void setImg(String img) throws Exception 
		{
		
		this.img = Util.getInstance().saveImage(img);
		
		}
	
	}
