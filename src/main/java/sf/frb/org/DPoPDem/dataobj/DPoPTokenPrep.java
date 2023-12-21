package sf.frb.org.DPoPDem.dataobj;

import com.nimbusds.jwt.SignedJWT;

public class DPoPTokenPrep {
	
	TokenBearerunc tkDPoPuc;
	
	SignedJWT signedJWT;
	
	String signedJWTSerialized;
	
	
	public String getSignedJWTSerialized() {
		return signedJWTSerialized;
	}


	public void setSignedJWTSerialized(String signedJWTSerialized) {
		this.signedJWTSerialized = signedJWTSerialized;
	}


	public TokenBearerunc getTkDPoPuc() {
		return tkDPoPuc;
	}


	public SignedJWT getSignedJWT() {
		return signedJWT;
	}


	public void setSignedJWT(SignedJWT signedJWT) {
		this.signedJWT = signedJWT;
	}


	public void setTkDPoPuc(TokenBearerunc tkDPoPuc) {
		this.tkDPoPuc = tkDPoPuc;
	}



	public DPoPTokenPrep() {
		// TODO Auto-generated constructor stub
	}

}
