package sf.frb.org.DPoPDem.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import sf.frb.org.DPoPDem.utils.JWTGenUtilNim;

public class EncTokenBean {
	
	@Autowired
	JWTGenUtilNim jwtUtilsNim;
	
	Map<String, String> headers;
	
	Logger LOG = LoggerFactory.getLogger(LoggingController.class);
	String[] authHeader;
	String bearerToken;
	String issuerIdp;
	String encToken;
	
	Map claimSet;
	
	String valStatus;

	
	
	public String getEncToken() {
		return encToken;
	}

	public void setEncToken(String encToken) {
		this.encToken = encToken;
	}

	public String getValStatus() {
		return valStatus;
	}

	public void setValStatus(String valStatus) {
		this.valStatus = valStatus;
	}

	public String getIssuerIdp() {
		return issuerIdp;
	}

	public void setIssuerIdp(String issuerIdp) {
		this.issuerIdp = issuerIdp;
	}




	public Map getClaimSet() {
		return claimSet;
	}

	public void setClaimSet(Map claimSet) {
		this.claimSet = claimSet;
	}


	public EncTokenBean(Map<String, String> hdr) {
		// TODO Auto-generated constructor stub
		this.setHeaders(hdr);
		this.setVars();
		
			
		
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> hdr) {
		this.headers = hdr;
	}

	private void setVars() {
		
		
	    headers.forEach((key, value) -> {
	        LOG.info(String.format("Header '%s' = %s", key, value));
	    });
	    
	    authHeader = (headers.get("authorization")).split(" ");
	    // Get only the accesstoken from the Authorization, skip "Bearer"
	    bearerToken = authHeader[1];
	    
	    
	    
	    
	    			    
	    
		
		this.setIssuerIdp(headers.get("issueridp"));
		
		
		
	}
	
	
	
	private String encToken(String bearerToken)
	
	{
		
		//
		
		 this.setEncToken(jwtUtilsNim.encJWTClaims(bearerToken));
		
		 return this.getEncToken();
		
		
	}
	
	
	
	
	
	
	
}
