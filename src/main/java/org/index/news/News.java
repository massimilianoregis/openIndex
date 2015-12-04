package org.index.news;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.index.Util;

@Entity
public class News {
@Id	
private String id;
private String title;
private String text;
private String image;
private Date creationDate;

public News(){
	init();
}
public News(String title, String text){
	this.title=title;
	this.text=text;
	init();
} 
public void init(){
	
	this.id= UUID.randomUUID().toString();
	this.creationDate=new Date();
	}
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public String getText() {
	return text;
}
public void setText(String text) {
	this.text = text;
}
public String getImage() {
	return image;
}
public void setImage(String image) throws Exception{
	this.image = Util.getInstance().saveImage(image);
}
public Date getCreationDate() {
	return creationDate;
}
public void setCreationDate(Date creationDate) {
	this.creationDate = creationDate;
}



}
