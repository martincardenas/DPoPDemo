package sf.frb.org.DPoPDem.dataobj;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TokenBearerunc {
	
	String access_token;
	String scope;
	String token_type;
	Integer expires_in;
	String DPoPToken;
	Map<String, List<String>> headertreeMap = new TreeMap<>();
	
	
	

	public Map<String, List<String>> getHeadertreeMap() {
		return headertreeMap;
	}


	public void setHeadertreeMap(Map<String, List<String>> headertreeMap) {
		this.headertreeMap = headertreeMap;
	}


	public String getDPoPToken() {
		return DPoPToken;
	}


	public void setDPoPToken(String dPoPToken) {
		DPoPToken = dPoPToken;
	}


	public TokenBearerunc() {
		// TODO Auto-generated constructor stub
	}


	public String getAccess_token() {
		return access_token;
	}


	public void setAccess_token(String access_token) {
		this.access_token = access_token;
		this.DPoPToken = access_token;
	}


	public String getScope() {
		return scope;
	}


	public void setScope(String scope) {
		this.scope = scope;
	}


	public String getToken_type() {
		return token_type;
	}


	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}


	public Integer getExpires_in() {
		return expires_in;
	}


	public void setExpires_in(Integer expires_in) {
		this.expires_in = expires_in;
	}

}
