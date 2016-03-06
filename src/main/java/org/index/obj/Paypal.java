package org.index.obj;

import javax.persistence.Embeddable;

import org.index.service.IndexService.View;

import com.fasterxml.jackson.annotation.JsonView;

@Embeddable
public class Paypal {
	@JsonView(View.Shop.Full.class)	private String clientId;
	@JsonView(View.Shop.Full.class) private String privacy;
	@JsonView(View.Shop.Full.class) private String agreement;
	
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getAgreement() {
		return agreement;
	}
	public String getPrivacy() {
		return privacy;
	}
	public void setAgreement(String agreement) {
		this.agreement = agreement;
	}
	public void setPrivacy(String privacy) {
		this.privacy = privacy;
	}
}
