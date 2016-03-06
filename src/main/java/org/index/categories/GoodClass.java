package org.index.categories;

import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.index.repository.Repositories;
import org.index.service.IndexService.View;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@JsonIgnoreProperties(ignoreUnknown=true)
public class GoodClass {
	@Id
	@JsonView(View.Shop.Full.class)
	private String id;
	@JsonView(View.Shop.Full.class)
	private String code;
	@JsonView(View.Shop.Full.class)
	private String IT;
	@JsonView(View.Shop.Full.class)
	private String EN;
	@JsonView(View.Shop.Full.class)
	private String parentId;
	
	public GoodClass() {
		this.id=UUID.randomUUID().toString();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getIt() {
		return IT;
	}
	public void setIt(String iT) {
		IT = iT;
	}
	public String getEn() {
		return EN;
	}
	public void setEn(String eN) {
		EN = eN;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	public void setChildren(List<GoodClass> classes){
		for(GoodClass item:classes)	{
			item.setParentId(this.id);
			Repositories.goodClass.save(item);
		}
		
	}
	public List<GoodClass> getChildren(){
		return Repositories.goodClass.findByParentId(this.id);
	}
	
}
