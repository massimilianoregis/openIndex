package org.index.obj;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.index.Util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Color {
	@Id	
	public String id;
	public String name;
	public String image;
	public Float qta;
	
	public Color()
		{
		this.id=UUID.randomUUID().toString();
		}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) throws Exception{
		this.image = Util.getInstance().saveImage(image);
	}
	public Float getQta() {
		return qta;
	}
	public void setQta(Float qta) {
		this.qta = qta;
	}
	
}
