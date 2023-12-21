package sf.frb.org.DPoPDem.utils;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import com.nimbusds.jwt.SignedJWT;




public interface IJWTFunct {
	
	
	public PublicKey getPublicKey() throws  InvalidKeySpecException, NoSuchAlgorithmException ;
	public PrivateKey getPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException;
	//public String genJWT();
	public String genJWT();
	SignedJWT genJWTDPoPAth();
	SignedJWT genJWTDPoPAth(String ath);
	
	//public Jws<Claims> parseJwt(String JWTString) throws InvalidKeySpecException, NoSuchAlgorithmException;


}
