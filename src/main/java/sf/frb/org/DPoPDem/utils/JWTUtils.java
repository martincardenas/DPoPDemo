package sf.frb.org.DPoPDem.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Properties;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.SignedJWT;



@Component
public class JWTUtils implements IJWTFunct {
	
	@Value("${private.key.file}")
	private String privatekeyfile;

	@Value("${claims.file}")
	private String claimsfile;

	@Value("${public.key.file}")
	private String publickeyfile;
	
	@Value("${iss}")
	String issuer;

	@Value("${sub}")
	String subj;

	@Value("${aud}")
	String aud;

	
	@Value("${htu}")
	String htu;
	
	
	@Value("${htm}")
	String htm;
	
	@Value("${htuPR}")
	String htuPR;
	
	
	@Value("${htmGET}")
	String htmGET;


	JWK publicJWK;


		
	@Value("classpath:/sf/frb/org/DPoPDem/conf/privatekey.pem")
	Resource privFile;

	@Value("classpath:/sf/frb/org/DPoPDem/conf/publickey.pem")
	Resource pubFile;

	Logger logger = LoggerFactory.getLogger(JWTUtils.class);



	public JWTUtils() {
		// TODO Auto-generated constructor stub
	}
	
	
	

	
	public PublicKey getPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
		// TODO Auto-generated method stub
		
        String publicFile=null;
       // try {
        	//InputStream privkeyAsStream = privFile.getInputStream();
        	
        	publicFile = asString(pubFile);
		//} catch (IOException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		// }
         
        String rsaPublicKey = publicFile;
        logger.debug(rsaPublicKey);

		
		
		rsaPublicKey = rsaPublicKey.replace("-----BEGIN PUBLIC KEY-----", "");
		rsaPublicKey = rsaPublicKey.replace("-----END PUBLIC KEY-----", "");
		
		rsaPublicKey = rsaPublicKey.replaceAll("\\n", "");
		rsaPublicKey = rsaPublicKey.replaceAll("\\r", "");

		
		
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(rsaPublicKey));
		KeyFactory kf = KeyFactory.getInstance("RSA");
		PublicKey publicKey = kf.generatePublic(keySpec);
		return publicKey;

		
	}


	
	public PrivateKey getPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
	       String prvFile=null;
	       // try {
	        	//InputStream privkeyAsStream = privFile.getInputStream();
	        	
	        	prvFile = asString(privFile);
			//} catch (IOException e) {
				// TODO Auto-generated catch block
			//	e.printStackTrace();
			// }
	         
	        String rsaPrivateKey = prvFile;
	        logger.debug(rsaPrivateKey);

			rsaPrivateKey = rsaPrivateKey.replace("-----BEGIN PRIVATE KEY-----", "");
			rsaPrivateKey = rsaPrivateKey.replace("-----BEGIN RSA PRIVATE KEY-----", "");
			rsaPrivateKey = rsaPrivateKey.replace("-----END PRIVATE KEY-----", "");
			rsaPrivateKey = rsaPrivateKey.replace("-----END RSA PRIVATE KEY-----", "");
	        
	        rsaPrivateKey = rsaPrivateKey.replaceAll("\\n", "");
	        rsaPrivateKey = rsaPrivateKey.replaceAll("\\r", "");
			//rsaPrivateKey = "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";


	        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(rsaPrivateKey));
	        KeyFactory kf = KeyFactory.getInstance("RSA");
	        PrivateKey privKey = kf.generatePrivate(keySpec);
	        return privKey;
	      

	}


	
	public String genJWT() {
		// TODO Auto-generated method stub
		return null;
		
	}
	
	
	public String genJWTDPoP() {
		// TODO Auto-generated method stub
		return null;
		
	}

	
	
    public static String asString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream())) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
	public JWK getPublicJWK() {
		
		JWK jwk=null;

		try {
			 jwk = new RSAKey.Builder((RSAPublicKey) this.getPublicKey())

					.keyUse(KeyUse.SIGNATURE).keyID(UUID.randomUUID().toString()).build();

			logger.info("JWK : " + jwk.toJSONString());

		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jwk;
	}

	public void setPublicJWK(JWK publicJWK) {
		this.publicJWK = publicJWK;
	}





	@Override
	public SignedJWT genJWTDPoPAth() {
		// TODO Auto-generated method stub
		return null;
	}





	@Override
	public SignedJWT genJWTDPoPAth(String ath) {
		// TODO Auto-generated method stub
		return null;
	}



}
