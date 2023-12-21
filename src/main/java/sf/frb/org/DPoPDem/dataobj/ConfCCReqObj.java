package sf.frb.org.DPoPDem.dataobj;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)

@JsonPropertyOrder ({"client_id","client_secret","scope","grant_type"})

public class ConfCCReqObj {
	
	@JsonProperty("client_id")	
	private String client_id;
	@JsonProperty("client_secret")	
	private String client_secret;
	@JsonProperty("scope")	
	private String scope;
	@JsonProperty("grant_type")	
	private String grant_type;


	
	
	
	
	
	@JsonProperty("client_secret")	
	public String getClient_secret() {
		return client_secret;
	}
	@JsonProperty("client_secret")	
	public void setClient_secret(String client_secret) {
		this.client_secret = client_secret;
	}
	@JsonProperty("scope")	
	public String getScope() {
		return scope;
	}
	@JsonProperty("scope")	
	public void setScope(String scope) {
		this.scope = scope;
	}


	@JsonProperty("client_id")	
	public String getClient_id() {
		return client_id;
	}

	@JsonProperty("client_id")	
	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}


	@JsonProperty("grant_type")	
	public String getgrant_type() {
		return grant_type;
	}


	@JsonProperty("grant_type")	
	public void setgrant_type(String grant_type) {
		this.grant_type = grant_type;
	}

}
