package org.index.obj;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name="wear")
public class Wear extends Item {
	
	
	@ElementCollection
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<String> taglie;
	
	public List<String> getTaglie() {
		return taglie;
	}
	public void setTaglie(List<String> taglie) {
		this.taglie = taglie;
	}
	
	
}
