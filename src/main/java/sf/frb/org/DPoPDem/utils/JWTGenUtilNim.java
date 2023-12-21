package sf.frb.org.DPoPDem.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.time.Instant;
import java.util.Properties;
import java.util.UUID;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jose.jwk.*;
import com.nimbusds.jose.jwk.gen.*;
import com.nimbusds.jwt.*;

import com.nimbusds.oauth2.sdk.dpop.*;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.token.DPoPAccessToken;
import com.nimbusds.oauth2.sdk.token.Tokens;

import sf.frb.org.DPoPDem.dataobj.ConfCCReqObj;

import com.nimbusds.oauth2.sdk.TokenRequest;
import com.nimbusds.oauth2.sdk.TokenResponse;
import com.nimbusds.oauth2.sdk.AuthorizationGrant;
import com.nimbusds.oauth2.sdk.ClientCredentialsGrant;
import com.nimbusds.oauth2.sdk.ErrorObject;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.oauth2.sdk.auth.ClientAuthentication;
import com.nimbusds.oauth2.sdk.auth.ClientSecretPost;
import com.nimbusds.oauth2.sdk.Scope;

import java.net.URI;

@Component
public class JWTGenUtilNim extends JWTUtils {

	JWTClaimsSet jwtClaims = null;
	SignedJWT signedJWT = null;

	public SignedJWT getSignedJWT() {
		return signedJWT;
	}

	public void setSignedJWT(SignedJWT signedJWT) {
		this.signedJWT = signedJWT;
	}

	public JWTClaimsSet getJwtClaims() {
		return jwtClaims;
	}

	public void setJwtClaims(JWTClaimsSet jwtClaims) {
		this.jwtClaims = jwtClaims;
	}

	public JWTGenUtilNim() {
		// TODO Auto-generated constructor stub
	}

	public String encJWTClaims(String JWT) {
		String encJWT = null;

		// Request JWT encrypted with RSA-OAEP-256 and 128-bit AES/GCM
		JWEHeader header = new JWEHeader(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A128GCM);

		// Create the encrypted JWT object
		EncryptedJWT jwt = new EncryptedJWT(header, this.getJwtClaims());

		// Create an encrypter with the specified public RSA key
		RSAEncrypter encrypter = null;
		try {
			encrypter = new RSAEncrypter((RSAPublicKey) getPublicKey());

			jwt.encrypt(encrypter);

			encJWT = jwt.serialize();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JOSEException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Do the actual encryption

		return encJWT;

	}

	public String encJWTSignedJWT(SignedJWT sJWT) {

		JWEObject jweObject = new JWEObject(
				new JWEHeader.Builder(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A256GCM).contentType("JWT") // required
																												// to
																												// indicate
																												// nested
																												// JWT
						.build(),
				new Payload(sJWT));

		// Encrypt with the recipient's public key
		try {
			jweObject.encrypt(new RSAEncrypter((RSAPublicKey) getPublicKey()));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JOSEException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Serialise to JWE compact form
		String jweString = jweObject.serialize();

		return jweString;

	}

	public String encJWTSerialJWT(String serJWT) {

		JWEObject jweObject = new JWEObject(
				new JWEHeader.Builder(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A256GCM).contentType("JWT") // required
																												// to
																												// indicate
																												// nested
																												// JWT
						.build(),
				new Payload(serJWT));

		// Encrypt with the recipient's public key
		try {
			jweObject.encrypt(new RSAEncrypter((RSAPublicKey) getPublicKey()));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JOSEException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Serialise to JWE compact form
		String jweString = jweObject.serialize();

		return jweString;

	}

	public String genJWTDPoP(DPoPProofFactory proofFactory, ConfCCReqObj confclreq) {
		// TODO Auto-generated method stub

		try {

			// Generate an EC key pair for signing the DPoP proofs with the
			// ES256 JWS algorithm. The OAuth 2.0 client should store this
			// key securely for the duration of its use.
			//ECKey jwk = new ECKeyGenerator(Curve.P_256).keyID("1").generate();
			

			// Construct the client credentials grant
			AuthorizationGrant clientGrant = new ClientCredentialsGrant();

			// Token request with DPoP for a public OAuth 2.0 client
			ClientID clientID = new ClientID(confclreq.getClient_id());

			Secret clientSecret = new Secret(confclreq.getClient_secret());
			ClientAuthentication clientAuth = new ClientSecretPost(clientID, clientSecret);

			// The request scope for the token (may be optional)
			Scope scope = new Scope(confclreq.getScope());

			// Make the token request
			TokenRequest tokenRequest = new TokenRequest(
					//new URI("https://ec2-54-210-193-246.compute-1.amazonaws.com/webapps/c2id/token"), clientAuth,
					new URI("https://localhost/webapps/c2id/token"), clientAuth,
					clientGrant, scope);

			HTTPRequest httpRequest = tokenRequest.toHTTPRequest();

			// Generate a new DPoP proof for the token request
			SignedJWT proof = proofFactory.createDPoPJWT(httpRequest.getMethod().name(), httpRequest.getURI());
			
			return proof.serialize(false);
			
			
			/*
			
			Instant now = Instant.now();
			// Create RSA-signer with the private key
			JWSSigner signer = new RSASSASigner(getPrivateKey());

			// Prepare JWT with claims set
			JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()

					.jwtID(UUID.randomUUID().toString()).claim("htu", htu).claim("htm", htm).issueTime(Date.from(now))
					.build();

			JOSEObjectType jwtType = new JOSEObjectType("dpop+jwt");

			// JWK jwk =
			// JWK.parse("{\"kty\":\"RSA\",\"e\":\"AQAB\",\"kid\":\"c2ec3110-2d88-4d89-a2e3-0b706a5e6d4c\",\"n\":\"gvieFDj1aYun3MoycdWvGWVbHUZAsBFUxhCSwL33DPSgDjJDGhoaI1UTjPYzVtsFwyHRGxmqjXvOfkVwHwB_bFLr4Q3OfDNox137GJQ-jpQhCegI9QRpJk06qo8Ycsl6-D49Z66DOlTADHXM7ZefFZC6dGWBiNCiDtORlxdh55an8MY96Qjk-Muwc-MndeldwVGCea4GHxC3YkKqu7u_VJxPhunLMGjZzNUWsXCFvuv8Pfcp-Y1EDGFQwGmIHuHt6hDXLS-bNahW9ammpl_NYM_aZkGMO7lvHNoQcl1FXnhTCgM8SbNClJhaTSfR4lpQRoAlEKlkHK0Ix7Ph1Anf1Q\"}");

			final JWSHeader jwtheader = new JWSHeader.Builder(JWSAlgorithm.RS256).type(jwtType).jwk(this.getPublicJWK())
					.build();

			// SignedJWT signedJWT = new SignedJWT(new
			// JWSHeader.Builder(JWSAlgorithm.RS256).build(), claimsSet);
			SignedJWT signedJWT = new SignedJWT(jwtheader, claimsSet);

			// Compute the RSA signature
			signedJWT.sign(signer);

			this.setJwtClaims(claimsSet);

			this.setSignedJWT(signedJWT);

			// -jPDm5Iq0SZnjKjCNS5Q15fokXZc8u0A
			return signedJWT.serialize();
			*/
		}  catch (JOSEException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		return null;
	}

	public String decJWT(String encJWT) {

		EncryptedJWT jwt = null;

		try {
			jwt = EncryptedJWT.parse(encJWT);

			// Create a decrypter with the specified private RSA key
			RSADecrypter decrypter = null;

			decrypter = new RSADecrypter(getPrivateKey());
			// Decrypt
			jwt.decrypt(decrypter);

			// Retrieve JWT claims
			System.out.println(jwt.getJWTClaimsSet().getIssuer());
			;
			System.out.println(jwt.getJWTClaimsSet().getSubject());
			System.out.println(jwt.getJWTClaimsSet().getAudience().size());
			System.out.println(jwt.getJWTClaimsSet().getExpirationTime());
			// System.out.println(jwt.getJWTClaimsSet().getNotBeforeTime());
			// System.out.println(jwt.getJWTClaimsSet().getIssueTime());
			System.out.println(jwt.getJWTClaimsSet().getJWTID());

			return jwt.getPayload().toString();

		} catch (ParseException | JOSEException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvalidKeySpecException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return null;

	}

	public String decJWEtoJWT(String encJWT) {

		try {

			// Parse the JWE string
			JWEObject jweObject = JWEObject.parse(encJWT);

			// Decrypt with private key
			jweObject.decrypt(new RSADecrypter(getPrivateKey()));

			// Extract payload

			String decPayload = jweObject.getPayload().toString();

			// System.out.println("Decoded Signed JWT:" + signedJWT.serialize());
			logger.info("Decoded JWT :" + decPayload);

			// Retrieve JWT claims

			return decPayload;

		} catch (ParseException | JOSEException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvalidKeySpecException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return null;

	}

	public String encJWTSharedJWT(String sJWT) {

		JWEObject jweObject = null;
		// Encrypt with the recipient's public key
		try {

			// Generate symmetric 128 bit AES key
			KeyGenerator keyGen = KeyGenerator.getInstance("AES");
			// keyGen.init(128);
			// SecretKey key = keyGen.generateKey();

			SecretKey key = (SecretKey) readKey();

			// Create the header
			JWEHeader header = new JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A128GCM);

			// Set the plain text
			Payload payload = new Payload(sJWT);

			// Create the JWE object and encrypt it
			jweObject = new JWEObject(header, payload);
			jweObject.encrypt(new DirectEncrypter(key));

			// Serialise to compact JOSE form...
			String jweString = jweObject.serialize();

			// Parse into JWE object again...
			jweObject = JWEObject.parse(jweString);

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JOSEException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Serialise to JWE compact form
		String jweString = jweObject.serialize();

		return jweString;

	}

	@SuppressWarnings("unused")
	private static void writeKey(Serializable rSAKeyObj) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("rSAKeyObj.ser")));
		oos.writeObject(rSAKeyObj);
		oos.close();
	}

	private static Object readKey() throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("rSAKeyObj.ser")));
		Object obj = ois.readObject();
		return obj;
	}

	@Override
	public SignedJWT genJWTDPoPAth(String ath) {
		// TODO Auto-generated method stub

		try {

			Instant now = Instant.now();
			// Create RSA-signer with the private key
			JWSSigner signer = new RSASSASigner(getPrivateKey());

			// Prepare JWT with claims set
			JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()

					.jwtID(UUID.randomUUID().toString()).claim("htu", htuPR).claim("htm", htmGET)
					.issueTime(Date.from(now)).claim("ath", ath).build();

			JOSEObjectType jwtType = new JOSEObjectType("dpop+jwt");

			// JWK jwk =
			// JWK.parse("{\"kty\":\"RSA\",\"e\":\"AQAB\",\"kid\":\"c2ec3110-2d88-4d89-a2e3-0b706a5e6d4c\",\"n\":\"gvieFDj1aYun3MoycdWvGWVbHUZAsBFUxhCSwL33DPSgDjJDGhoaI1UTjPYzVtsFwyHRGxmqjXvOfkVwHwB_bFLr4Q3OfDNox137GJQ-jpQhCegI9QRpJk06qo8Ycsl6-D49Z66DOlTADHXM7ZefFZC6dGWBiNCiDtORlxdh55an8MY96Qjk-Muwc-MndeldwVGCea4GHxC3YkKqu7u_VJxPhunLMGjZzNUWsXCFvuv8Pfcp-Y1EDGFQwGmIHuHt6hDXLS-bNahW9ammpl_NYM_aZkGMO7lvHNoQcl1FXnhTCgM8SbNClJhaTSfR4lpQRoAlEKlkHK0Ix7Ph1Anf1Q\"}");

			final JWSHeader jwtheader = new JWSHeader.Builder(JWSAlgorithm.RS256).type(jwtType).jwk(this.getPublicJWK())
					.build();

			// SignedJWT signedJWT = new SignedJWT(new
			// JWSHeader.Builder(JWSAlgorithm.RS256).build(), claimsSet);
			SignedJWT signedJWT = new SignedJWT(jwtheader, claimsSet);

			// Compute the RSA signature
			signedJWT.sign(signer);

			this.setJwtClaims(claimsSet);

			this.setSignedJWT(signedJWT);
			logger.info("SIgnedJWT : " + signedJWT.serialize());

			// -jPDm5Iq0SZnjKjCNS5Q15fokXZc8u0A

			return signedJWT;

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JOSEException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	

	

}
